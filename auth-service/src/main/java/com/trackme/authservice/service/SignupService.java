package com.trackme.authservice.service;

import com.trackme.authservice.events.OnSignUpCompleteEvent;
import com.trackme.authservice.repository.RoleRepository;
import com.trackme.authservice.repository.UserRepository;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.constants.ConstantMessages;
import com.trackme.models.exception.ExpiredTokenException;
import com.trackme.models.exception.NotFoundException;
import com.trackme.models.exception.UserAlreadyExistException;
import com.trackme.models.payload.request.signup.SignupRequest;
import com.trackme.models.security.RoleEntity;
import com.trackme.models.security.UserEntity;
import com.trackme.models.security.VerificationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SignupService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final VerificationTokenService verificationTokenService;

    public CommonResponse processSignup(SignupRequest request) throws UserAlreadyExistException {

        userRepository.findByUsername(request.getUsername())
                .ifPresent((user) -> {
                    throw new UserAlreadyExistException("Username [" + user.getUsername() + "] already exists.");
                });

        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    throw new UserAlreadyExistException("Email [" + user.getEmail() + "] already exists.");
                });

        if (!request.getRole().startsWith("ROLE_")) {
            request.setRole("ROLE_" + request.getRole());
        }
        String requestedRole = request.getRole();

        RoleEntity role = roleRepository.findByRoleName(requestedRole)
                .orElseThrow(() -> {
                    throw new NotFoundException("Requested Role [" + requestedRole + "] not found.");
                });

        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .orgId(request.getOrgId())
                .role(role)
                .build();

        UserEntity savedUser = userRepository.save(user);

        if (savedUser == null) {
            return CommonResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ConstantMessages.USER_NOT_SAVED);
        }

        eventPublisher.publishEvent(new OnSignUpCompleteEvent(savedUser));

        return CommonResponse.ok();
    }

    public CommonResponse signupConfirmation(String token) {

        VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);

        UserEntity user = verificationToken.getUser();

        if (verificationToken.getExpiryDate() -
                LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() <= 0) {
            throw new ExpiredTokenException("provided token has expired");
        }

        verificationTokenService.deleteToken(verificationToken.getToken());

        user.setEnabled(true);
        UserEntity savedUser = userRepository.save(user);

        user = savedUser;
        return CommonResponse.ok(user);
    }

    public CommonResponse resendVerification(String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("user with email [" + userEmail + "] not found"));

        verificationTokenService.createVerificationToken(user, UUID.randomUUID().toString());

        // TODO send email
        return CommonResponse.ok();
    }
}

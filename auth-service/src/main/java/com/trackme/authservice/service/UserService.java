package com.trackme.authservice.service;

import com.trackme.authservice.repository.RoleRepository;
import com.trackme.authservice.repository.UserRepository;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.constants.ConstantMessages;
import com.trackme.models.exception.RoleNotFoundException;
import com.trackme.models.exception.UserAlreadyExistException;
import com.trackme.models.payload.request.signup.SignupRequest;
import com.trackme.models.security.RoleEntity;
import com.trackme.models.security.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

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
                    throw new RoleNotFoundException("Requested Role [" + requestedRole + "] not found.");
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

        return CommonResponse.ok();
    }
}

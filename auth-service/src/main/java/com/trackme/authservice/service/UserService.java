package com.trackme.authservice.service;

import com.trackme.authservice.repository.UserRepository;
import com.trackme.authservice.utils.SecurityUtils;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.constants.ConstantMessages;
import com.trackme.models.payload.request.retrieveuser.RetrieveUserRequest;
import com.trackme.models.security.RoleEntity;
import com.trackme.models.security.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserEntity findUserByUsername(RetrieveUserRequest request) {

        log.info("making user is retrieving their info");
        String authUsername = SecurityUtils.getUsername();

        if (!authUsername.equals(request.getUsername())) {
            log.error("User [{}] not allowed to retrieve user [{}] info",
                    authUsername, request.getUsername());
            throw new UnauthorizedUserException("User is not allowed to retrieve " + request.getUsername() + "'s into");
        }

        log.info("finding user for username [{}]", request.getUsername());

        UserEntity userEntity = userRepository.findByUsername(request.getUsername())
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                "user with username [" + request.getUsername() + "] is not found")
                );

        return userEntity;
    }

    public UserEntity findUserByEmail(String email) {
        log.info("finding user with email [{}]", email);
        UserEntity user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User with email [" + email + "] is not found.")
        );
        return user;
    }

    public UserEntity updateUserRoles(UserEntity user, RoleEntity role) {

        user.setRoles(new ArrayList<>());
        user.getRoles().add(role);

        log.info("updated user [{}] with role [{}]", user.getEmail(), role.getRoleName());
        log.info("updating user in database");

        // update user in database
        return userRepository.save(user);
    }
}

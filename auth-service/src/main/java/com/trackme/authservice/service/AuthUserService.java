package com.trackme.authservice.service;

import com.trackme.authservice.repository.UserRepository;
import com.trackme.common.security.SecurityUtils;
import com.trackme.models.security.RoleEntity;
import com.trackme.models.security.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthUserService {

    private final UserRepository userRepository;

    public UserEntity findUserByUsername() {

        log.info("getting username form auth token");
        String authUsername = SecurityUtils.getUsername();

        log.info("finding user for username [{}]", authUsername);

        UserEntity userEntity = userRepository.findByUsername(authUsername)
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                "user with username [" + authUsername + "] is not found")
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

package com.trackme.authservice.service;

import com.trackme.authservice.Base;
import com.trackme.authservice.repository.UserRepository;
import com.trackme.models.security.RoleEntity;
import com.trackme.models.security.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthUserServiceTest extends Base {

    @Autowired
    AuthUserService authUserService;

    @MockBean
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("Find User By Username Tests")
    @Nested
    class FindUserByUsernameTests {
        @Test
        @WithMockUser(username = "test-user", roles = {"PM"})
        public void findUserByUsername_Valid() {
            UserEntity returnedUser = UserEntity.builder().username("test-user")
                    .build();

            when(userRepository.findByUsername(any(String.class)))
                    .thenReturn(Optional.of(returnedUser));

            UserEntity user = authUserService.findUserByUsername();

            assertEquals(returnedUser, user);
        }
    }

    @DisplayName("Find User By email Tests")
    @Nested
    class FindUserByEmailTests {
        @Test
        public void findUserByEmail_Found_Valid() {
            String email = "test@email.com";

            UserEntity userEntity = UserEntity.builder().email(email).build();
            when(userRepository.findByEmail(any(String.class)))
                    .thenReturn(Optional.ofNullable(userEntity));

            UserEntity user = authUserService.findUserByEmail(email);

            assertEquals(email, user.getEmail());
        }

        @Test
        public void findUserByEmail_NotFound_Invalid() {
            String email = "test@email.com";
            String exceptionMessage = "User with email [" + email + "] is not found.";
            when(userRepository.findByEmail(anyString()))
                    .thenThrow(
                            new UsernameNotFoundException(exceptionMessage)
                    );

            UsernameNotFoundException exception = assertThrows(
                    UsernameNotFoundException.class,
                    () -> {
                        authUserService.findUserByEmail(email);
                    }
            );

            assertEquals(exceptionMessage, exception.getMessage());
        }
    }

    @DisplayName("Update User Roles Tests")
    @Nested
    class UpdateUserRolesTests {

        @Test
        public void updateUserRoles_Valid() {
            RoleEntity role = RoleEntity.builder().roleName("ROLE_ADMIN").build();
            RoleEntity roleToUpdated = RoleEntity.builder().roleName("ROLE_PM").build();
            UserEntity user = UserEntity.builder().email("test@email.com")
                    .role(role).build();

            when(userRepository.save(any(UserEntity.class)))
                    .thenReturn(user);

            UserEntity userEntity = authUserService.updateUserRoles(user, roleToUpdated);

            assertEquals(roleToUpdated, userEntity.getRoles().get(0));
        }
    }

}
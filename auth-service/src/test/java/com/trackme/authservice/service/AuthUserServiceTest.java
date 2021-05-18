package com.trackme.authservice.service;

import com.trackme.authservice.Base;
import com.trackme.authservice.repository.UserRepository;
import com.trackme.authservice.utils.OrgService;
import com.trackme.models.enums.RoleEnum;
import com.trackme.models.payload.request.retrieveuser.GetUserDetailsRequest;
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

    @DisplayName("Find Auth User By Username Tests")
    @Nested
    class FindAuthUserByUsernameTests {
        @Test
        @WithMockUser(username = "test-user", roles = {"PM"})
        public void findAuthUserByUsername_Valid() {
            UserEntity returnedUser = UserEntity.builder().username("test-user")
                    .build();

            when(userRepository.findByUsername(any(String.class)))
                    .thenReturn(Optional.of(returnedUser));

            UserEntity user = authUserService.findAuthUser();

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

    @DisplayName("Find User By username Tests")
    @Nested
    class FindUserByUsernameTests {
        @Test
        public void findUserByUsername_Found_Valid() {
            String username = "test";

            UserEntity userEntity = UserEntity.builder().username(username).build();
            when(userRepository.findByUsername(any(String.class)))
                    .thenReturn(Optional.ofNullable(userEntity));

            UserEntity user = authUserService.findUserByUsername(username);

            assertEquals(username, user.getUsername());
        }

        @Test
        public void findUserByUsername_NotFound_Invalid() {
            String username = "test";
            String exceptionMessage = "User with username [" + username + "] is not found.";
            when(userRepository.findByUsername(anyString()))
                    .thenThrow(
                            new UsernameNotFoundException(exceptionMessage)
                    );

            UsernameNotFoundException exception = assertThrows(
                    UsernameNotFoundException.class,
                    () -> {
                        authUserService.findUserByUsername(username);
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


    @DisplayName("Find By Username Or Email Tests")
    @Nested
    class FindByUsernameOrEmailTests {
        GetUserDetailsRequest request;
        AuthUserService spyUserService;
        OrgService spyOrgService;
        UserEntity admin;
        UserEntity pm;

        @BeforeEach
        void setUp() {
            request = GetUserDetailsRequest.builder()
                    .username("test").email("test@test.com")
                    .build();
            spyOrgService = spy(new OrgService(userRepository));
            spyUserService = spy(new AuthUserService(userRepository, spyOrgService));

            admin = UserEntity.builder()
                    .username("admin").email("admin@email.com")
                    .role(RoleEntity.builder()
                            .roleName(RoleEnum.ADMIN.getRoleName()).build())
                    .build();

            pm = UserEntity.builder()
                    .username("pm").email("pm@email.com").orgId("test-org")
                    .role(RoleEntity.builder()
                            .roleName(RoleEnum.PM.getRoleName()).build())
                    .build();
        }

        @Test
        public void findByUsernameOrEmail_Username_Found_Valid() {
            request.setEmail(null);
            doReturn(admin).when(spyUserService).findUserByEmail(any(String.class));
            doReturn(admin).when(spyUserService).findUserByUsername(any(String.class));
            doReturn(pm).when(spyUserService).findAuthUser();
            doNothing().when(spyOrgService).checkSameOrg(any(UserEntity.class),
                    any(UserEntity.class));

            UserEntity foundUser = spyUserService.findByUsernameOrEmail(request);

            verify(spyUserService, times(0)).findUserByEmail(anyString());
            assertNotNull(foundUser.getRoles());
            assertEquals(admin.getEmail(), foundUser.getEmail());
            assertEquals(admin.getUsername(), foundUser.getUsername());
        }

        @Test
        public void findByUsernameOrEmail_Email_Found_Valid() {
            request.setUsername(null);
            doReturn(pm).when(spyUserService).findUserByEmail(any(String.class));
            doReturn(pm).when(spyUserService).findUserByUsername(any(String.class));
            doReturn(admin).when(spyUserService).findAuthUser();
            doNothing().when(spyOrgService).checkSameOrg(any(UserEntity.class),
                    any(UserEntity.class));

            UserEntity foundUser = spyUserService.findByUsernameOrEmail(request);

            verify(spyUserService, times(0)).findUserByUsername(anyString());
            assertNotNull(foundUser.getRoles());
            assertEquals(pm.getEmail(), foundUser.getEmail());
            assertEquals(pm.getUsername(), foundUser.getUsername());
        }
    }
}
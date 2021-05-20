package com.trackme.authservice.service;

import com.trackme.authservice.Base;
import com.trackme.authservice.repository.UserRepository;
import com.trackme.authservice.utils.OrgService;
import com.trackme.common.security.SecurityUtils;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.enums.RoleEnum;
import com.trackme.models.exception.InvalidCredentialsException;
import com.trackme.models.payload.request.retrieveuser.GetUserDetailsRequest;
import com.trackme.models.payload.request.user.password.UserChangePasswordRequest;
import com.trackme.models.security.RoleEntity;
import com.trackme.models.security.UserEntity;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthUserServiceTest extends Base {

    @Autowired
    AuthUserService authUserService;

    @MockBean
    UserRepository userRepository;

    UserEntity user;
    RoleEntity role;
    RoleEntity roleToUpdated;

    @BeforeEach
    void setUp() {
        role = RoleEntity.builder().roleName("ROLE_ADMIN").build();
        roleToUpdated = RoleEntity.builder().roleName("ROLE_PM").build();
        user = UserEntity.builder().username("user")
                .email("user@email.com").role(role)
                .password(passwordEncoder.encode("oldPassword"))
                .build();

    }

    @DisplayName("Find Auth User By Username Tests")
    @Nested
    class FindAuthUserByUsernameTests {
        MockedStatic<SecurityUtils> mockedStatic;

        @BeforeEach
        void setUp() {
            mockedStatic = mockStatic(SecurityUtils.class);
        }

        @AfterEach
        void tearDown() {
            mockedStatic.close();
        }

        @Test
        public void findAuthUserByUsername_Valid() {
            when(userRepository.findByUsername(any(String.class)))
                    .thenReturn(Optional.of(user));
            mockedStatic.when(SecurityUtils::getUsername).thenReturn("test-user");

            UserEntity user = authUserService.findAuthUser();

            assertEquals(user, user);
        }
    }

    @DisplayName("Find User By email Tests")
    @Nested
    class FindUserByEmailTests {
        @Test
        public void findUserByEmail_Found_Valid() {
            when(userRepository.findByEmail(any(String.class)))
                    .thenReturn(Optional.ofNullable(user));

            UserEntity result = authUserService.findUserByEmail(user.getEmail());

            assertEquals(user.getEmail(), result.getEmail());
        }

        @Test
        public void findUserByEmail_NotFound_Invalid() {
            when(userRepository.findByEmail(anyString()))
                    .thenThrow(
                            new UsernameNotFoundException("")
                    );

            assertThrows(
                    UsernameNotFoundException.class,
                    () -> authUserService.findUserByEmail(user.getEmail())
            );
        }
    }

    @DisplayName("Find User By username Tests")
    @Nested
    class FindUserByUsernameTests {
        @Test
        public void findUserByUsername_Found_Valid() {

            when(userRepository.findByUsername(any(String.class)))
                    .thenReturn(Optional.ofNullable(user));

            UserEntity result = authUserService.findUserByUsername(user.getUsername());

            assertEquals(user.getUsername(), result.getUsername());
            assertEquals(user.getEmail(), result.getEmail());
        }

        @Test
        public void findUserByUsername_NotFound_Invalid() {
            when(userRepository.findByUsername(anyString()))
                    .thenThrow(
                            new UsernameNotFoundException("")
                    );

            assertThrows(
                    UsernameNotFoundException.class,
                    () -> authUserService.findUserByUsername(user.getUsername())
            );
        }
    }

    @DisplayName("Update User Roles Tests")
    @Nested
    class UpdateUserRolesTests {

        @Test
        public void updateUserRoles_Valid() {
            when(userRepository.save(any(UserEntity.class)))
                    .thenReturn(user);

            UserEntity result = authUserService.updateUserRoles(user, roleToUpdated);

            assertEquals(user.getUsername(), result.getUsername());
            assertEquals(user.getEmail(), result.getEmail());
            assertEquals(user.getRoles().get(0), result.getRoles().get(0));
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
            spyUserService = spy(new AuthUserService(userRepository, spyOrgService, passwordEncoder));

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

    @DisplayName("Change Password Tests")
    @Nested
    class ChangePasswordTests {
        MockedStatic<SecurityUtils> mockedStatic;
        UserChangePasswordRequest userChangePasswordRequest;

        @BeforeEach
        void setUp() {
            mockedStatic = mockStatic(SecurityUtils.class);
            userChangePasswordRequest = UserChangePasswordRequest.builder()
                    .oldPassword("oldPassword")
                    .newPassword("newPassword")
                    .build();
        }

        @AfterEach
        void tearDown() {
            mockedStatic.close();
        }

        @Test
        public void changePassword_Valid() {
            mockedStatic.when(SecurityUtils::getUsername).thenReturn("user");
            when(userRepository.findByUsername(any(String.class)))
                    .thenReturn(Optional.of(user));
            when(userRepository.save(any(UserEntity.class)))
                    .thenReturn(user);

            CommonResponse response =
                    authUserService.changePassword(userChangePasswordRequest);

            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertTrue(response.isSuccess());
            assertNull(response.getPayload());
            assertNull(response.getError());
        }

        @Test
        public void changePassword_UserNotFound_Invalid() {
            mockedStatic.when(SecurityUtils::getUsername).thenReturn("user");
            when(userRepository.findByUsername(any(String.class)))
                    .thenThrow(new UsernameNotFoundException(""));
            when(userRepository.save(any(UserEntity.class)))
                    .thenReturn(user);

            assertThrows(
                    UsernameNotFoundException.class,
                    () -> authUserService.changePassword(userChangePasswordRequest)
            );
        }

        @Test
        public void changePassword_OldPasswordDoesntMatch_Invalid() {
            mockedStatic.when(SecurityUtils::getUsername).thenReturn("user");
            when(userRepository.findByUsername(any(String.class)))
                    .thenReturn(Optional.of(user));
            when(userRepository.save(any(UserEntity.class)))
                    .thenReturn(user);

            user.setPassword(passwordEncoder.encode(
                    userChangePasswordRequest.getOldPassword() + "q"));
            assertThrows(
                    InvalidCredentialsException.class,
                    () -> authUserService.changePassword(userChangePasswordRequest)
            );
        }

        @Test
        public void changePassword_NewPasswordSameAsOld_Invalid() {
            mockedStatic.when(SecurityUtils::getUsername).thenReturn("user");
            when(userRepository.findByUsername(any(String.class)))
                    .thenReturn(Optional.of(user));
            when(userRepository.save(any(UserEntity.class)))
                    .thenReturn(user);

            user.setPassword(passwordEncoder.encode(
                    userChangePasswordRequest.getNewPassword()));

            assertThrows(
                    InvalidCredentialsException.class,
                    () -> authUserService.changePassword(userChangePasswordRequest)
            );
        }

        @Test
        public void changePassword_FailedToSave_Invalid() {
            mockedStatic.when(SecurityUtils::getUsername).thenReturn("user");
            when(userRepository.findByUsername(any(String.class)))
                    .thenReturn(Optional.of(user));
            when(userRepository.save(any(UserEntity.class)))
                    .thenThrow(new RuntimeException(""));

            assertThrows(
                    RuntimeException.class,
                    () -> authUserService.changePassword(userChangePasswordRequest)
            );
        }
    }
}
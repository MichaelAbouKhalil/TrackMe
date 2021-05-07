package com.trackme.userservice.service;

import com.trackme.common.service.UserService;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.payload.request.profile.ProfileRequest;
import com.trackme.models.profile.ProfileEntity;
import com.trackme.models.security.RoleEntity;
import com.trackme.models.security.UserEntity;
import com.trackme.userservice.Base;
import com.trackme.userservice.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProfileServiceTest extends Base {

    @Autowired
    ProfileService profileService;

    @MockBean
    UserService userService;

    @MockBean
    ProfileRepository profileRepository;

    ProfileEntity profileEntity;
    UserEntity userEntity;
    ProfileRequest profileRequest;

    @BeforeEach
    void setUp() {
        userEntity = UserEntity.builder()
                .username("demo-admin")
                .email("demo@admin.com")
                .role(RoleEntity.builder().roleName("ROLE_ADMIN").build())
                .build();
        profileEntity = ProfileEntity.builder()
                .username("demo-admin")
                .firstName("demo")
                .lastName("admin")
                .userEntity(userEntity)
                .build();
        profileRequest = ProfileRequest.builder()
                .firstName("demo")
                .lastName("admin")
                .build();
    }

    @DisplayName("Find Profile By Username Tests")
    @Nested
    class FindProfileByUsernameTests {
        @Test
        public void findProfileByUsername_Found() {
            when(profileRepository.findById(any(String.class)))
                    .thenReturn(Optional.ofNullable(profileEntity));

            Optional<ProfileEntity> optionalProfile = profileService.findProfileByUsername("demo-admin");

            assertTrue(optionalProfile.isPresent());
        }

        @Test
        public void findProfileByUsername_NotFound() {
            when(profileRepository.findById(any(String.class)))
                    .thenReturn(Optional.ofNullable(null));

            Optional<ProfileEntity> optionalProfile = profileService.findProfileByUsername("demo-admin");

            assertFalse(optionalProfile.isPresent());
        }
    }

    @DisplayName("Get Complete Profile Tests")
    @Nested
    class GetCompleteProfileTests {
        @Test
        public void getCompleteProfile_ExistingProfile_Valid() {
            when(userService.getUser()).thenReturn(userEntity);

            ProfileService spy = spy(new ProfileService(profileRepository, userService));
            doReturn(Optional.of(profileEntity)).when(spy)
                    .findProfileByUsername(any(String.class));

            CommonResponse response = spy.getCompleteUserProfile();

            assertTrue(response.isSuccess());
            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertNull(response.getError());
            assertEquals(profileEntity.getFirstName(), ((ProfileEntity) response.getPayload()).getFirstName());
            assertEquals(userEntity, ((ProfileEntity) response.getPayload()).getUserEntity());

        }

        @Test
        public void getCompleteProfile_NotExistingProfile_Valid() {
            when(userService.getUser()).thenReturn(userEntity);

            ProfileService spy = spy(new ProfileService(profileRepository, userService));
            doReturn(Optional.ofNullable(null)).when(spy)
                    .findProfileByUsername(any(String.class));

            CommonResponse response = spy.getCompleteUserProfile();

            assertTrue(response.isSuccess());
            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertNull(response.getError());
            assertNull(((ProfileEntity) response.getPayload()).getFirstName());
            assertEquals(userEntity.getUsername(), ((ProfileEntity) response.getPayload()).getUsername());
            assertEquals(userEntity, ((ProfileEntity) response.getPayload()).getUserEntity());

        }

        @Test
        public void getCompleteProfile_NoUser_Invalid() {
            when(userService.getUser()).thenReturn(null);

            UsernameNotFoundException exception = assertThrows(
                    UsernameNotFoundException.class,
                    () -> {
                        profileService.getCompleteUserProfile();
                    }
            );

            assertEquals("user is not found", exception.getMessage());

        }
    }

    @DisplayName("Save/Update Profile Tests")
    @Nested
    class SaveUpdateProfileTests {

        @Test
        @WithMockUser(username = "demo-admin", roles = {"ADMIN"})
        public void saveUpdateProfile_ProfileFound_Valid() {
            ProfileService spy = spy(new ProfileService(profileRepository, userService));
            doReturn(Optional.of(profileEntity)).when(spy)
                    .findProfileByUsername(any(String.class));

            when(profileRepository.save(any(ProfileEntity.class)))
                    .thenReturn(profileEntity);

            CommonResponse response = profileService.saveUpdateProfile(profileRequest);

            assertTrue(response.isSuccess());
            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertNotNull(response.getPayload());
            assertNull(response.getError());
        }

        @Test
        @WithMockUser(username = "demo-admin", roles = {"ADMIN"})
        public void saveUpdateProfile_ProfileNotFound_Valid() {
            ProfileService spy = spy(new ProfileService(profileRepository, userService));

            doReturn(Optional.ofNullable(null)).when(spy)
                    .findProfileByUsername(any(String.class));

            when(profileRepository.save(any(ProfileEntity.class)))
                    .thenReturn(profileEntity);

            CommonResponse response = profileService.saveUpdateProfile(profileRequest);

            assertTrue(response.isSuccess());
            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertNotNull(response.getPayload());
            assertNull(response.getError());
        }

        @Test
        @WithMockUser(username = "demo-admin", roles = {"ADMIN"})
        public void saveUpdateProfile_SaveError_Invalid() {
            ProfileService spy = spy(new ProfileService(profileRepository, userService));
            doReturn(Optional.of(profileEntity)).when(spy)
                    .findProfileByUsername(any(String.class));

            when(profileRepository.save(any(ProfileEntity.class)))
                    .thenReturn(null);

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> spy.saveUpdateProfile(profileRequest)
            );

            assertEquals("Failed to save user in database", ex.getMessage());
        }

    }
}
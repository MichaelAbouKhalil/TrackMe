package com.trackme.userservice.controller;

import com.trackme.models.common.CommonResponse;
import com.trackme.models.payload.request.profile.ProfileRequest;
import com.trackme.models.profile.ProfileEntity;
import com.trackme.userservice.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProfileControllerTest extends BaseController {

    private static final String BASE_API = "/profile";

    @MockBean
    ProfileService profileService;

    ProfileRequest request;

    @BeforeEach
    void setUp() {
        request = ProfileRequest.builder()
                .firstName("firstName").lastName("lastName").build();
    }

    @DisplayName("Get Profile Tests")
    @Nested
    class GetProfileTests {

        @Test
        public void getProfile_NoAuth_Invalid() throws Exception {

            mockMvc.perform(get(BASE_API))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                    .andExpect(jsonPath("$.success").value("false"))
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void getProfile_Authenticated_Valid() throws Exception {
            when(profileService.getCompleteUserProfile())
                    .thenReturn(CommonResponse.ok(new ProfileEntity()));

            String accessToken = accessTokenUtil.obtainAccessToken("demo-admin", "demo-admin");

            mockMvc.perform(get(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("AUTHORIZATION", "Bearer " + accessToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.payload").exists());
        }
    }

    @DisplayName("Save/Update Profile Tests")
    @Nested
    class SaveUpdateProfileTests {

        @DisplayName("Save/Update Request Validation Tests")
        @Nested
        class SaveUpdateRequestValidationTests {

            @Test
            public void saveUpdateProfile_RequestValidation_Valid() throws Exception {
                when(profileService.saveUpdateProfile(any(ProfileRequest.class)))
                        .thenReturn(CommonResponse.ok(new ProfileEntity()));

                String accessToken = accessTokenUtil
                        .obtainAccessToken("demo-admin", "demo-admin");

                mockMvc.perform(post(BASE_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("AUTHORIZATION", "Bearer " + accessToken))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                        .andExpect(jsonPath("$.success").value("true"))
                        .andExpect(jsonPath("$.payload").exists());
            }

            @Test
            public void saveUpdateProfile_RequestValidation_NoFirstName_Invalid() throws Exception {
                when(profileService.saveUpdateProfile(any(ProfileRequest.class)))
                        .thenReturn(CommonResponse.ok(new ProfileEntity()));

                String accessToken = accessTokenUtil
                        .obtainAccessToken("demo-admin", "demo-admin");

                request.setFirstName("");

                mockMvc.perform(post(BASE_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("AUTHORIZATION", "Bearer " + accessToken))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                        .andExpect(jsonPath("$.success").value("false"))
                        .andExpect(jsonPath("$.error").exists())
                        .andExpect(jsonPath("$.error.errorMessage").exists());
            }

            @Test
            public void saveUpdateProfile_RequestValidation_NoLastName_Invalid() throws Exception {
                when(profileService.saveUpdateProfile(any(ProfileRequest.class)))
                        .thenReturn(CommonResponse.ok(new ProfileEntity()));

                String accessToken = accessTokenUtil.obtainAccessToken("demo-admin", "demo-admin");

                request.setLastName("");

                mockMvc.perform(post(BASE_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("AUTHORIZATION", "Bearer " + accessToken))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                        .andExpect(jsonPath("$.success").value("false"))
                        .andExpect(jsonPath("$.error").exists())
                        .andExpect(jsonPath("$.error.errorMessage").exists());

            }
        }

        @Test
        public void saveUpdateProfile_NoAuth_Invalid() throws Exception {

            mockMvc.perform(post(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                    .andExpect(jsonPath("$.success").value("false"))
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void saveUpdateProfile_Authenticated_Valid() throws Exception {
            when(profileService.saveUpdateProfile(any(ProfileRequest.class)))
                    .thenReturn(CommonResponse.ok(new ProfileEntity()));

            String accessToken = accessTokenUtil
                    .obtainAccessToken("demo-admin", "demo-admin");

            mockMvc.perform(post(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header("AUTHORIZATION", "Bearer " + accessToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.payload").exists());
        }
    }
}
package com.trackme.authservice.controller;

import com.trackme.authservice.service.AuthUserService;
import com.trackme.models.payload.request.retrieveuser.RetrieveUserRequest;
import com.trackme.models.security.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
class UserControllerTest extends BaseController {

    private static final String BASE_API = "/user";

    @MockBean
    AuthUserService authUserService;

    UserEntity user;
    RetrieveUserRequest request;

    @BeforeEach
    void setUp() {
        user = UserEntity.builder()
                .username("test-username").build();
        request = RetrieveUserRequest.builder().username("test").build();
    }

    @DisplayName("Retrieve User Access Tests")
    @Nested
    class RetrieveUserAccessTests {
        @Test
        public void retrieveUser_NoAuth_Invalid() throws Exception {
            when(authUserService.findUserByUsername())
                    .thenReturn(user);

            mockMvc.perform(get(BASE_API)
                    .content(objectMapper.writeValueAsString(request))
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("false"))
                    .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void retrieveUser_ADMIN_Valid() throws Exception {
            when(authUserService.findUserByUsername())
                    .thenReturn(user);

            String accessToken = accessTokenUtil.obtainAccessToken(
                    "demo-admin", "demo-admin");

            ResultActions result = mockMvc.perform(get(BASE_API)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("AUTHORIZATION", "Bearer " + accessToken)
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist());

        }

        @Test
        public void retrieveUser_PM_Valid() throws Exception {
            when(authUserService.findUserByUsername())
                    .thenReturn(user);

            String accessToken = accessTokenUtil.obtainAccessToken(
                    "demo-pm", "demo-pm");

            ResultActions result = mockMvc.perform(get(BASE_API)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("AUTHORIZATION", "Bearer " + accessToken)
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist());

        }

        @Test
        public void retrieveUser_DEV_Valid() throws Exception {
            when(authUserService.findUserByUsername())
                    .thenReturn(user);

            String accessToken = accessTokenUtil.obtainAccessToken(
                    "demo-dev", "demo-dev");

            ResultActions result = mockMvc.perform(get(BASE_API)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("AUTHORIZATION", "Bearer " + accessToken)
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist());

        }

        @Test
        public void retrieveUser_CUSTOMER_Valid() throws Exception {
            when(authUserService.findUserByUsername())
                    .thenReturn(user);

            String accessToken = accessTokenUtil.obtainAccessToken(
                    "demo-customer", "demo-customer");

            ResultActions result = mockMvc.perform(get(BASE_API)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("AUTHORIZATION", "Bearer " + accessToken)
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist());

        }
    }
}
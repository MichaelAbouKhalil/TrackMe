package com.trackme.authservice.controller;

import com.trackme.authservice.service.SignupService;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.constants.ConstantMessages;
import com.trackme.models.enums.PendingRoleEnum;
import com.trackme.models.exception.UserAlreadyExistException;
import com.trackme.models.payload.request.signup.SignupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SignupControllerTest extends BaseController {

    private static final String BASE_ROOT = "/signup";

    @MockBean
    SignupService signupService;

    SignupRequest validRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        validRequest = SignupRequest.builder()
                .username("a-username")
                .password("$2y$10$7wgYSzR3P.zdlEm3S5FyuO6uqHSDFlKezzQFZAFyCwcx2TdCePftK")
                .email("test@test.com")
                .role(PendingRoleEnum.PM_PENDING.name())
                .orgId("123")
                .build();
    }

    @DisplayName("Signup Response Tests")
    @Nested
    class SignupResponseTests {
        @Test
        public void signup_Success() throws Exception {

            when(signupService.processSignup(any(SignupRequest.class))).thenReturn(CommonResponse.ok());

            mockMvc.perform(post(BASE_ROOT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.status").value("200"));
        }

        @Test
        public void signup_Failure() throws Exception {

            when(signupService.processSignup(any(SignupRequest.class)))
                    .thenReturn(CommonResponse.error(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(), ConstantMessages.USER_NOT_SAVED
                    ));

            mockMvc.perform(post(BASE_ROOT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("false"))
                    .andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .andExpect(jsonPath("$.error.errorMessage").value(ConstantMessages.USER_NOT_SAVED));
        }

        @Test
        public void signup_UserAlreadyExistException() throws Exception {

            when(signupService.processSignup(any(SignupRequest.class)))
                    .thenThrow(new UserAlreadyExistException("User Already found"));

            mockMvc.perform(post(BASE_ROOT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("false"))
                    .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.error.errorMessage").value("User Already found"));
        }
    }

    @DisplayName("Signup Validation Tests")
    @Nested
    class SignupValidationTests {
        @Test
        public void signup_ValidRequest() throws Exception {
            when(signupService.processSignup(any(SignupRequest.class))).thenReturn(CommonResponse.ok());

            mockMvc.perform(post(BASE_ROOT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isOk());
        }

        @Test
        public void signup_InvalidUsername() throws Exception {
            validRequest.setUsername("a");

            mockMvc.perform(post(BASE_ROOT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("400"))
                    .andExpect(jsonPath("$.error.errorMessage").isNotEmpty())
                    .andExpect(jsonPath("$.success").value("false"));
        }

        @Test
        public void signup_InvalidPasswordCypher() throws Exception {
            validRequest.setPassword("password");

            mockMvc.perform(post(BASE_ROOT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("400"))
                    .andExpect(jsonPath("$.error.errorMessage").isNotEmpty())
                    .andExpect(jsonPath("$.success").value("false"));
        }

        @Test
        public void signup_InvalidEmail() throws Exception {
            validRequest.setEmail("test@test");

            mockMvc.perform(post(BASE_ROOT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("400"))
                    .andExpect(jsonPath("$.error.errorMessage").isNotEmpty())
                    .andExpect(jsonPath("$.success").value("false"));
        }

        @Test
        public void signup_EmptyOrgId() throws Exception {
            validRequest.setOrgId("");

            mockMvc.perform(post(BASE_ROOT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("400"))
                    .andExpect(jsonPath("$.error.errorMessage").isNotEmpty())
                    .andExpect(jsonPath("$.success").value("false"));
        }

    }

}
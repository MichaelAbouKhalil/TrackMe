package com.trackme.authservice.controller;

import com.trackme.authservice.service.AuthUserService;
import com.trackme.models.payload.request.retrieveuser.GetUserDetailsRequest;
import com.trackme.models.security.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
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

    private static final String BASE_API_AUTH_USER = "/user";
    private static final String BASE_API_USER_DETAILS = "/userDetails";

    @MockBean
    AuthUserService authUserService;

    UserEntity user;
    GetUserDetailsRequest getUserDetailsRequest;

    @BeforeEach
    void setUp() {
        user = UserEntity.builder()
                .username("test-username").build();
        getUserDetailsRequest = GetUserDetailsRequest.builder()
                .email("test@email.com").build();
    }

    @DisplayName("Retrieve User Access Tests")
    @Nested
    class RetrieveUserAccessTests {
        @Test
        public void retrieveUser_NoAuth_Invalid() throws Exception {
            when(authUserService.findAuthUser())
                    .thenReturn(user);

            mockMvc.perform(get(BASE_API_AUTH_USER)
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("false"))
                    .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void retrieveUser_ADMIN_Valid() throws Exception {
            when(authUserService.findAuthUser())
                    .thenReturn(user);

            ResultActions result = mockMvc.perform(get(BASE_API_AUTH_USER)
                    .header("AUTHORIZATION", "Bearer " + adminToken)
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist());

        }

        @Test
        public void retrieveUser_PM_Valid() throws Exception {
            when(authUserService.findAuthUser())
                    .thenReturn(user);

            ResultActions result = mockMvc.perform(get(BASE_API_AUTH_USER)
                    .header("AUTHORIZATION", "Bearer " + pmToken)
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist());

        }

        @Test
        public void retrieveUser_DEV_Valid() throws Exception {
            when(authUserService.findAuthUser())
                    .thenReturn(user);

            ResultActions result = mockMvc.perform(get(BASE_API_AUTH_USER)
                    .header("AUTHORIZATION", "Bearer " + devToken)
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist());

        }

        @Test
        public void retrieveUser_CUSTOMER_Valid() throws Exception {
            when(authUserService.findAuthUser())
                    .thenReturn(user);

            ResultActions result = mockMvc.perform(get(BASE_API_AUTH_USER)
                    .header("AUTHORIZATION", "Bearer " + custToken)
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist());

        }
    }

    @DisplayName("Get User Details Access Tests")
    @Nested
    class GetUserDetailsAccessTests {
        @Test
        public void getUserDetails_NoAuth_Invalid() throws Exception {
            when(authUserService.findByUsernameOrEmail(any(GetUserDetailsRequest.class)))
                    .thenReturn(user);

            mockMvc.perform(get(BASE_API_USER_DETAILS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(getUserDetailsRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists())
                    .andExpect(jsonPath("$.payload").doesNotExist());
        }

        @Test
        public void getUserDetails_ADMIN_Valid() throws Exception {
            when(authUserService.findByUsernameOrEmail(any(GetUserDetailsRequest.class)))
                    .thenReturn(user);

            mockMvc.perform(get(BASE_API_USER_DETAILS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("AUTHORIZATION", "Bearer " + adminToken)
                    .content(objectMapper.writeValueAsString(getUserDetailsRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist());
        }

        @Test
        public void getUserDetails_PM_Valid() throws Exception {
            when(authUserService.findByUsernameOrEmail(any(GetUserDetailsRequest.class)))
                    .thenReturn(user);

            mockMvc.perform(get(BASE_API_USER_DETAILS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("AUTHORIZATION", "Bearer " + pmToken)
                    .content(objectMapper.writeValueAsString(getUserDetailsRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist());
        }

        @Test
        public void getUserDetails_DEV_Valid() throws Exception {
            when(authUserService.findByUsernameOrEmail(any(GetUserDetailsRequest.class)))
                    .thenReturn(user);

            mockMvc.perform(get(BASE_API_USER_DETAILS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("AUTHORIZATION", "Bearer " + devToken)
                    .content(objectMapper.writeValueAsString(getUserDetailsRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist());
        }

        @Test
        public void getUserDetails_CUST_Valid() throws Exception {
            when(authUserService.findByUsernameOrEmail(any(GetUserDetailsRequest.class)))
                    .thenReturn(user);

            mockMvc.perform(get(BASE_API_USER_DETAILS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("AUTHORIZATION", "Bearer " + custToken)
                    .content(objectMapper.writeValueAsString(getUserDetailsRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist());
        }
    }
}
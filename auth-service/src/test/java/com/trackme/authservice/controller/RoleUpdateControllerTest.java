package com.trackme.authservice.controller;

import com.trackme.authservice.service.RoleUpdateService;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.payload.request.roleupdate.RoleUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RoleUpdateControllerTest extends BaseController {

    private final static String PROMOTE_API = "/role/promote";
    private final static String DEMOTE_API = "/role/demote";

    @MockBean
    RoleUpdateService roleUpdateService;

    RoleUpdateRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        request = RoleUpdateRequest.builder().email("test@signup.com").build();
    }

    @DisplayName("Request validation Tests")
    @Nested
    class requestValidationTests {
        @Test
        public void roleUpdate_ValidRequest() throws Exception {
            when(roleUpdateService.updateRole(any(RoleUpdateRequest.class), eq(true)))
                    .thenReturn(CommonResponse.ok());

            mockMvc.perform(post(PROMOTE_API + "/pm")
                    .header("Authorization", "Bearer " + adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.status").value("200"));
        }

        @Test
        public void roleUpdate_InvalidEmail() throws Exception {
            when(roleUpdateService.updateRole(any(RoleUpdateRequest.class), eq(true)))
                    .thenReturn(CommonResponse.ok());

            request.setEmail("invalid@email");

            mockMvc.perform(post(PROMOTE_API + "/pm")
                    .header("Authorization", "Bearer " + adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("400"))
                    .andExpect(jsonPath("$.error.errorMessage").isNotEmpty())
                    .andExpect(jsonPath("$.success").value("false"));
        }
    }

    @DisplayName("Promote Pm API access Tests")
    @Nested
    class promotePmTests {
        @Test
        public void promotePm_NoAuth() throws Exception {

            mockMvc.perform(post(PROMOTE_API + "/pm")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("false"))
                    .andExpect(jsonPath("$.status").value("401"));
        }

        @Test
        public void promotePm_Admin() throws Exception {
            when(roleUpdateService.updateRole(any(RoleUpdateRequest.class), eq(true)))
                    .thenReturn(CommonResponse.ok());

            mockMvc.perform(post(PROMOTE_API + "/pm")
                    .header("Authorization", "Bearer " + adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.status").value("200"));
        }

        @Test
        public void promotePm_NotAuthorized() throws Exception {

            ResultActions resultActions = mockMvc.perform(post(PROMOTE_API + "/pm")
                    .header("Authorization", "Bearer " + pmToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("false"))
                    .andExpect(jsonPath("$.status").value("403"));
        }
    }


    @DisplayName("Promote DEV/CUSTOMER API access Tests")
    @Nested
    class promoteDevCustomerTests {
        @Test
        public void promoteDev_NoAuth_Invalid() throws Exception {

            mockMvc.perform(post(PROMOTE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("false"))
                    .andExpect(jsonPath("$.status").value("401"));
        }

        @Test
        public void promoteDev_Admin_Valid() throws Exception {
            when(roleUpdateService.updateRole(any(RoleUpdateRequest.class), eq(true)))
                    .thenReturn(CommonResponse.ok());

            mockMvc.perform(post(PROMOTE_API)
                    .header("Authorization", "Bearer " + adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.status").value("200"));
        }

        @Test
        public void promoteCustomer_PM_Valid() throws Exception {
            when(roleUpdateService.updateRole(any(RoleUpdateRequest.class), eq(true)))
                    .thenReturn(CommonResponse.ok());

            mockMvc.perform(post(PROMOTE_API)
                    .header("Authorization", "Bearer " + pmToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.status").value("200"));
        }

        @Test
        public void promoteDev_NotAuthorized_Invalid() throws Exception {

            mockMvc.perform(post(PROMOTE_API)
                    .header("Authorization", "Bearer " + devToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("false"))
                    .andExpect(jsonPath("$.status").value("403"));
        }

        @Test
        public void promoteCustomer_NotAuthorized_Invalid() throws Exception {

            mockMvc.perform(post(PROMOTE_API)
                    .header("Authorization", "Bearer " + custToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("false"))
                    .andExpect(jsonPath("$.status").value("403"));
        }
    }

    @DisplayName("Demote Pm API access Tests")
    @Nested
    class demotePmTests {
        @Test
        public void demotePm_NoAuth() throws Exception {

            mockMvc.perform(post(DEMOTE_API + "/pm")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("false"))
                    .andExpect(jsonPath("$.status").value("401"));
        }

        @Test
        public void demotePm_Admin() throws Exception {
            when(roleUpdateService.updateRole(any(RoleUpdateRequest.class), eq(false)))
                    .thenReturn(CommonResponse.ok());

            mockMvc.perform(post(DEMOTE_API + "/pm")
                    .header("Authorization", "Bearer " + adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.status").value("200"));
        }

        @Test
        public void demotePm_NotAuthorized() throws Exception {

            ResultActions resultActions = mockMvc.perform(post(DEMOTE_API + "/pm")
                    .header("Authorization", "Bearer " + pmToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("false"))
                    .andExpect(jsonPath("$.status").value("403"));
        }
    }

    @DisplayName("Demote DEV/CUSTOMER API access Tests")
    @Nested
    class demoteDevCustomerTests {
        @Test
        public void demoteDev_NoAuth_Invalid() throws Exception {

            mockMvc.perform(post(DEMOTE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("false"))
                    .andExpect(jsonPath("$.status").value("401"));
        }

        @Test
        public void demoteDev_Admin_Valid() throws Exception {
            when(roleUpdateService.updateRole(any(RoleUpdateRequest.class), eq(false)))
                    .thenReturn(CommonResponse.ok());

            mockMvc.perform(post(DEMOTE_API)
                    .header("Authorization", "Bearer " + adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.status").value("200"));
        }

        @Test
        public void demoteCustomer_PM_Valid() throws Exception {
            when(roleUpdateService.updateRole(any(RoleUpdateRequest.class), eq(false)))
                    .thenReturn(CommonResponse.ok());

            mockMvc.perform(post(DEMOTE_API)
                    .header("Authorization", "Bearer " + pmToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.status").value("200"));
        }

        @Test
        public void demoteDev_NotAuthorized_Invalid() throws Exception {

            mockMvc.perform(post(DEMOTE_API)
                    .header("Authorization", "Bearer " + devToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("false"))
                    .andExpect(jsonPath("$.status").value("403"));
        }

        @Test
        public void demoteCustomer_NotAuthorized_Invalid() throws Exception {

            mockMvc.perform(post(DEMOTE_API)
                    .header("Authorization", "Bearer " + custToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("false"))
                    .andExpect(jsonPath("$.status").value("403"));
        }
    }
}
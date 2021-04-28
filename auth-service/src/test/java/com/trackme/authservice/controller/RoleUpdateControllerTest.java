package com.trackme.authservice.controller;

import com.trackme.authservice.service.RoleService;
import com.trackme.authservice.utils.AccessTokenUtil;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.enums.RoleEnum;
import com.trackme.models.payload.request.roleupdate.RoleUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoleUpdateControllerTest extends BaseIT {

    private final static String PROMOTE_API = "/role/promote";
    private final static String DEMOTE_API = "/role/demote";

    @MockBean
    RoleService roleService;

    @Autowired
    private AccessTokenUtil accessTokenUtil;

    RoleUpdateRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        request = RoleUpdateRequest.builder().email("test@signup.com").build();
    }

    @DisplayName("Request validation Tests")
    @Nested
    class requestValidationTests{
        @Test
        public void roleUpdate_ValidRequest() throws Exception {
            when(roleService.promote(any(RoleUpdateRequest.class), RoleEnum.PM)).thenReturn(CommonResponse.ok());

            String accessToken = accessTokenUtil.obtainAccessToken("demo-admin", "demo-admin");

            mockMvc.perform(post(PROMOTE_API + "/pm")
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.status").value("200"));
        }

        @Test
        public void roleUpdate_InvalidEmail() throws Exception {
            when(roleService.promote(any(RoleUpdateRequest.class), RoleEnum.PM)).thenReturn(CommonResponse.ok());

            String accessToken = accessTokenUtil.obtainAccessToken("demo-admin", "demo-admin");

            request.setEmail("invalid@email");

            mockMvc.perform(post(PROMOTE_API + "/pm")
                    .header("Authorization", "Bearer " + accessToken)
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
            when(roleService.promote(any(RoleUpdateRequest.class), RoleEnum.PM)).thenReturn(CommonResponse.ok());

            String accessToken = accessTokenUtil.obtainAccessToken("demo-admin", "demo-admin");

            mockMvc.perform(post(PROMOTE_API + "/pm")
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("true"))
                    .andExpect(jsonPath("$.status").value("200"));
        }

        @Test
        public void promotePm_NotAuthorized() throws Exception {

            String accessToken = accessTokenUtil.obtainAccessToken("demo-pm", "demo-pm");

            ResultActions resultActions = mockMvc.perform(post(PROMOTE_API + "/pm")
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value("false"))
                    .andExpect(jsonPath("$.status").value("403"));
        }
    }

}
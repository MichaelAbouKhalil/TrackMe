package com.trackme.authservice.controller;

import com.trackme.authservice.service.RoleService;
import com.trackme.authservice.utils.AccessTokenUtil;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.payload.request.roleupdate.RoleUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.ArrayList;
import java.util.List;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void promotePm_NoAuth() throws Exception {

        mockMvc.perform(post(PROMOTE_API + "/pm")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        RoleUpdateRequest.builder().email("test@signup.com").build())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void promotePm_Admin() throws Exception {
        when(roleService.promote(any(RoleUpdateRequest.class))).thenReturn(CommonResponse.ok());

        String accessToken = accessTokenUtil.obtainAccessToken("demo-admin", "demo-admin");

        mockMvc.perform(post(PROMOTE_API + "/pm")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        RoleUpdateRequest.builder().email("test@signup.com").build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.status").value("200"));
    }

    @Test
    public void promotePm_NotAuthorized() throws Exception{

        String accessToken = accessTokenUtil.obtainAccessToken("demo-pm", "demo-pm");

        mockMvc.perform(post(PROMOTE_API + "/pm")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        RoleUpdateRequest.builder().email("test@signup.com").build())))
                .andExpect(status().isForbidden());
    }
}
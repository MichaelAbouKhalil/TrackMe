package com.trackme.ticketservice.controller;

import com.trackme.ticketservice.Base;
import com.trackme.ticketservice.utils.AccessTokenUtil;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseController extends Base {

    @Autowired
    WebApplicationContext wac;

    @Autowired
    AccessTokenUtil accessTokenUtil;

    ObjectMapper objectMapper = new ObjectMapper();

    public MockMvc mockMvc;

    String adminToken;
    String pmToken;
    String devToken;
    String custToken;

    @BeforeAll
    void setup() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(springSecurity())
                .build();

        adminToken = accessTokenUtil.obtainAccessToken("demo-admin", "demo-admin");
        pmToken = accessTokenUtil.obtainAccessToken("demo-pm", "demo-pm");
        devToken = accessTokenUtil.obtainAccessToken("demo-dev", "demo-dev");
        custToken = accessTokenUtil.obtainAccessToken("demo-customer", "demo-customer");
    }
}

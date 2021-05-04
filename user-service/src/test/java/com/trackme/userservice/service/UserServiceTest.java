package com.trackme.userservice.service;

import com.trackme.models.common.CommonResponse;
import com.trackme.models.security.UserEntity;
import com.trackme.userservice.Base;
import com.trackme.userservice.proxy.AuthServiceFeignProxy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserServiceTest extends Base {

    @Autowired
    UserService userService;

    @MockBean
    AuthServiceFeignProxy authServiceFeignProxy;

    @DisplayName("Get User From Auth Service Tests")
    @Nested
    class GetUserTests {

        @Test
        @WithMockUser(username = "demo-admin")
        public void getUser_Success_Valid() {
            when(authServiceFeignProxy.retrieveUser())
                    .thenReturn(CommonResponse.ok(
                            UserEntity.builder()
                                    .username("demo-admin")
                                    .id(1L)
                                    .build()
                    ));

            UserEntity user = userService.getUser();

            assertNotNull(user);
            assertEquals(1L, user.getId());
            assertEquals("demo-admin", user.getUsername());
        }

        @Test
        @WithMockUser(username = "demo-admin")
        public void getUser_Failed_Invalid() {
            when(authServiceFeignProxy.retrieveUser())
                    .thenReturn(CommonResponse.error());

            UserEntity user = userService.getUser();

            assertNull(user);
        }
    }

}
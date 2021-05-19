package com.trackme.userservice.service;

import com.trackme.common.proxy.auth.AuthServiceFeignProxy;
import com.trackme.common.security.SecurityUtils;
import com.trackme.common.service.UserService;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.security.UserEntity;
import com.trackme.userservice.Base;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class UserServiceTest extends Base {

    @Autowired
    UserService userService;

    @MockBean
    AuthServiceFeignProxy authServiceFeignProxy;

    @DisplayName("Get User From Auth Service Tests")
    @Nested
    class GetUserTests {
        MockedStatic<SecurityUtils> mockedStatic;
        @BeforeEach
        void setUp() {
            mockedStatic = mockStatic(SecurityUtils.class);
        }

        @AfterEach
        void tearDown() {
            mockedStatic.close();
        }

        @Test
        public void getUser_Success_Valid() {
            mockedStatic.when(SecurityUtils::getUserRoles).thenReturn(new String[]{"ROLE_ADMIN"});
            mockedStatic.when(SecurityUtils::getUsername).thenReturn("demo-admin");
            mockedStatic.when(SecurityUtils::getUserEmail).thenReturn("admin@demo.com");
            mockedStatic.when(SecurityUtils::getUserId).thenReturn(1L);
            mockedStatic.when(SecurityUtils::getUserOrgId).thenReturn("test-org");

            UserEntity user = userService.getUser();

            assertNotNull(user);
            assertEquals(1L, user.getId());
            assertEquals("demo-admin", user.getUsername());
        }
    }

}
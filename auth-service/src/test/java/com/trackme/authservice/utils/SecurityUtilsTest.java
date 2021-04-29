package com.trackme.authservice.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SecurityUtilsTest {

    @Test
    @WithMockUser(username = "admin")
    public void getUsername_Authenticated_Valid() {

        String username = SecurityUtils.getUsername();

        assertEquals("admin", username);
    }

    @Test
    public void getUsername_Unauthenticated_Invalid() {
        UnauthorizedUserException unauthorizedUserException = assertThrows(UnauthorizedUserException.class,
                () -> {
                    SecurityUtils.getUsername();
                });

        assertNotNull(unauthorizedUserException);
        assertEquals("Unauthorized/Unauthenticated user exception", unauthorizedUserException.getMessage());
    }
}
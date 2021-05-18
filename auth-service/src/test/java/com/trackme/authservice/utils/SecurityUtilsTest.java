package com.trackme.authservice.utils;

import com.trackme.authservice.Base;
import com.trackme.common.security.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;


class SecurityUtilsTest extends Base {

    @Test
    @WithMockUser(username = "admin")
    public void getUsername_Authenticated_Valid() {

        String username = SecurityUtils.getUsernameFromContext();

        assertEquals("admin", username);
    }

    @Test
    public void getUsername_Unauthenticated_Invalid() {
        UnauthorizedUserException unauthorizedUserException = assertThrows(UnauthorizedUserException.class,
                () -> {
                    SecurityUtils.getUsernameFromContext();
                });

        assertNotNull(unauthorizedUserException);
        assertEquals("Unauthorized/Unauthenticated user exception", unauthorizedUserException.getMessage());
    }
}
package com.trackme.common.security;

import com.trackme.models.constants.JwtFields;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SecurityUtils {

    private static Object getAuthenticatedUser() {
        if (SecurityContextHolder.getContext() == null
                || SecurityContextHolder.getContext().getAuthentication() == null
                || SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null) {

            throw new UnauthorizedUserException("Unauthorized/Unauthenticated user exception");
        }

        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static String getUsername() {
        return FilterUtils.getUsername();
    }

    public static String getUserEmail() {
        return FilterUtils.getUserEmail();
    }

    public static Long getUserId() {
        return FilterUtils.getUserId();
    }

    public static String getUserOrgId() {
        return FilterUtils.getUserOrgId();
    }

    public static String[] getUserRoles() {
        return FilterUtils.getUserRoles();
    }
}

package com.trackme.common.security;

import com.trackme.models.common.JwtToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;

public class SecurityUtils {

    private static JwtToken getJwtToken() {
        return MySecurityContextHolder.getContext().getJwtToken();
    }

    public static String getUsername() {
        return getJwtToken().getUsername();
    }

    public static String getUserEmail() {
        return getJwtToken().getEmail();
    }

    public static Long getUserId() {
        return getJwtToken().getUserId();
    }

    public static String getUserOrgId() {
        return getJwtToken().getOrgId();
    }

    public static String[] getUserRoles() {
        return getJwtToken().getAuthorities();
    }

    private static Object getAuthenticatedUser() {
        if (SecurityContextHolder.getContext() == null
                || SecurityContextHolder.getContext().getAuthentication() == null
                || SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null) {

            throw new UnauthorizedUserException("Unauthorized/Unauthenticated user exception");
        }

        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static String getUsernameFromContext() {
        Object authObject = getAuthenticatedUser();
        if (authObject instanceof User) {
            return ((User) authObject).getUsername();
        } else if (authObject instanceof String) {
            return (String) authObject;
        }

        throw new UnauthorizedUserException("Authentication is not instance on User or String, " + authObject);
    }
}

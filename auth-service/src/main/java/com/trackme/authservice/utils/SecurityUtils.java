package com.trackme.authservice.utils;

import com.trackme.models.security.UserDetail;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;

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
        Object authObject = getAuthenticatedUser();
        if (authObject instanceof User) {
            return ((User) authObject).getUsername();
        } else if (authObject instanceof String) {
            return (String) authObject;
        }

        throw new UnauthorizedUserException("Authentication is not instance on User or String, " + authObject);
    }
}

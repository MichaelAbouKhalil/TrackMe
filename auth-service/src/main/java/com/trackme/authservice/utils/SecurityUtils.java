package com.trackme.authservice.utils;

import com.trackme.models.security.UserDetail;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;

public class SecurityUtils {

    private static User getAuthenticatedUser(){
        if(SecurityContextHolder.getContext() == null
        || SecurityContextHolder.getContext().getAuthentication() == null
        || SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null){

            throw new UnauthorizedUserException("Unauthorized/Unauthenticated user exception");
        }

        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static String getUsername(){
        return getAuthenticatedUser().getUsername();
    }
}

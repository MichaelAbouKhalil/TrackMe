package com.trackme.common.security;

import com.trackme.models.common.JwtToken;

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
}

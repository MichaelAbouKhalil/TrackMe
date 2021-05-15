package com.trackme.common.security;

import com.trackme.models.common.JwtToken;

public class JwtContext {

    private static final ThreadLocal<JwtToken> jwtTokenThreadLocal = new ThreadLocal<>();

    public static JwtToken getJwtToken() {
        return jwtTokenThreadLocal.get();
    }

    public static void setJwtToken(JwtToken jwtToken) {
        jwtTokenThreadLocal.set(jwtToken);
    }
}

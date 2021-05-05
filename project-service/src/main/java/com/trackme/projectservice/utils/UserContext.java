package com.trackme.projectservice.utils;

import org.springframework.stereotype.Component;

@Component
public class UserContext {

    public static final String CORRELATION_ID = "tm-correlation-id";
    public static final String AUTH_TOKEN = "Authorization";

    private static final ThreadLocal<String> correlationId = new ThreadLocal<>();
    private static final ThreadLocal<String> authToken = new ThreadLocal<>();

    public static String getCorrelationId() {
        return correlationId.get();
    }

    public static void setCorrelationId(String cId) {
        correlationId.set(cId);
    }

    public static String getAuthToken() {
        return authToken.get();
    }

    public static void setAuthToken(String aToken) {
        authToken.set(aToken);
    }

}

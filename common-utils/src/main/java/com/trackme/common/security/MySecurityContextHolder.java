package com.trackme.common.security;

import com.trackme.models.common.JwtToken;
import io.jsonwebtoken.lang.Assert;

public class MySecurityContextHolder {

    private static final ThreadLocal<JwtContext> jwtContext = new ThreadLocal<>();

    public static final JwtContext getContext() {
        JwtContext context = jwtContext.get();

        if (context == null) {
            context = createNewContext();
            jwtContext.set(context);
        }

        return jwtContext.get();
    }

    public static final void setContext(JwtContext context) {
        Assert.notNull(context, "Only non-null jwtContexts are permitted");
        jwtContext.set(context);
    }

    private static JwtContext createNewContext() {
        return new JwtContext();
    }
}

package com.trackme.userservice.utils;

import org.springframework.util.Assert;

public class UserContextHolder {

    private static final ThreadLocal<UserContext> userContext = new ThreadLocal<>();

    public static final UserContext getContext(){
        UserContext context = userContext.get();

        if(context == null){
            context = createNewContext();
            userContext.set(context);
        }

        return userContext.get();
    }

    public static final void setContext(UserContext context){
        Assert.notNull(context, "Only non-null userContexts are permitted");
        userContext.set(context);
    }

    public static UserContext createNewContext() {
        return new UserContext();
    }
}

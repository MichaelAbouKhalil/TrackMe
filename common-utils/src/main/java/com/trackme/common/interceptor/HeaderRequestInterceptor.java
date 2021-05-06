package com.trackme.common.interceptor;

import com.trackme.common.usercontext.UserContext;
import com.trackme.common.usercontext.UserContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class HeaderRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(UserContext.AUTH_TOKEN, UserContextHolder.getContext().getAuthToken());
    }
}

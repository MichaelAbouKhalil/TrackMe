package com.trackme.common.proxy.auth;

import com.trackme.models.common.CommonResponse;
import com.trackme.models.security.UserEntity;
import com.trackme.common.interceptor.HeaderRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "auth-service",
        configuration = {HeaderRequestInterceptor.class}
)
public interface AuthServiceFeignProxy {

    @GetMapping("/user")
    ResponseEntity<CommonResponse<UserEntity>> retrieveUser();

    @GetMapping("/userDetails")
    ResponseEntity<CommonResponse<UserEntity>> getUserDetails();
}


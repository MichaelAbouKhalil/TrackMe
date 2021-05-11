package com.trackme.common.proxy.auth;

import com.trackme.models.common.CommonResponse;
import com.trackme.models.payload.request.retrieveuser.GetUserDetailsRequest;
import com.trackme.models.security.UserEntity;
import com.trackme.common.interceptor.HeaderRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "auth-service",
        configuration = {HeaderRequestInterceptor.class}
)
public interface AuthServiceFeignProxy {

    @GetMapping("/user")
    ResponseEntity<CommonResponse<UserEntity>> retrieveUser();

    @PostMapping("/userDetails")
    ResponseEntity<CommonResponse<UserEntity>> getUserDetails(GetUserDetailsRequest request);
}


package com.trackme.userservice.proxy;

import com.trackme.models.common.CommonResponse;
import com.trackme.models.security.UserEntity;
import com.trackme.userservice.utils.request.HeaderRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
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
}


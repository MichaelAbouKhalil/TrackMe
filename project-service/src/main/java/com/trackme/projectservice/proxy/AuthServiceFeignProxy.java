package com.trackme.projectservice.proxy;

import com.trackme.models.common.CommonResponse;
import com.trackme.models.security.UserEntity;
import com.trackme.projectservice.utils.request.HeaderRequestInterceptor;
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
}


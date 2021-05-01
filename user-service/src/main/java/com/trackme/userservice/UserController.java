package com.trackme.userservice;

import com.trackme.models.common.CommonResponse;
import com.trackme.userservice.proxy.AuthServiceFeignProxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final AuthServiceFeignProxy authServiceFeignProxy;

    @GetMapping("/user")
    public ResponseEntity<CommonResponse> getUser(){

        CommonResponse response = authServiceFeignProxy.retrieveUser();
        log.info(response.toString());

        return ResponseEntity.ok().body(response);
    }

}

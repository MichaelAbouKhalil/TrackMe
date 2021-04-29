package com.trackme.authservice.controller;


import com.trackme.authservice.service.SignupService;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.payload.request.signup.SignupRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SignupController {

    private final SignupService signupService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse> signup(@Valid @RequestBody SignupRequest request){
        log.debug("received request on signup()...");

        CommonResponse response = signupService.processSignup(request);

        return ResponseEntity.ok().body(response);
    }
}

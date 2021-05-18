package com.trackme.authservice.controller;


import com.trackme.authservice.service.SignupService;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.payload.request.signup.ResendVerificationEmailRequest;
import com.trackme.models.payload.request.signup.SignupRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SignupController {

    private final SignupService signupService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse> signup(@Valid @RequestBody SignupRequest request) {
        log.debug("received request on signup()...");

        CommonResponse response = signupService.processSignup(request);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/signupConfirmation/{token}")
    public ResponseEntity<CommonResponse> signupConfirmation(@PathVariable("token") String token) {
        log.debug("received request on signupConfirmation()");
        CommonResponse response = signupService.signupConfirmation(token);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/signup/resend")
    public ResponseEntity<CommonResponse> resendVerification(@RequestBody ResendVerificationEmailRequest request){
        log.debug("received request on resendVerification()");
        CommonResponse response = signupService.resendVerification(request.getEmail());

        return ResponseEntity.ok().body(response);
    }
}

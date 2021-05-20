package com.trackme.authservice.controller;

import com.trackme.authservice.service.AuthUserService;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.payload.request.retrieveuser.GetUserDetailsRequest;
import com.trackme.models.payload.request.user.password.UserChangePasswordRequest;
import com.trackme.models.security.UserEntity;
import com.trackme.models.security.permission.AuthenticatedPermission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final AuthUserService authUserService;

    @GetMapping("/user")
    @AuthenticatedPermission
    public ResponseEntity<CommonResponse> retrieveUser() {
        log.info("received request on retrieveUser()");
        UserEntity user = authUserService.findAuthUser();

        CommonResponse response = CommonResponse.ok(user);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/userDetails") // this needs to be post because feign will change from get to post if request body exists
    @AuthenticatedPermission
    public ResponseEntity<CommonResponse> getUserDetails(@Valid @RequestBody GetUserDetailsRequest request) {
        log.info("received request on getUserDetails()");
        UserEntity user = authUserService.findByUsernameOrEmail(request);

        CommonResponse<UserEntity> response = CommonResponse.ok(user);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/user/changePassword")
    @AuthenticatedPermission
    public ResponseEntity<CommonResponse> changePassword(
            @Valid @RequestBody UserChangePasswordRequest request) {
        log.info("received request on changePassword()");

        CommonResponse response = authUserService.changePassword(request);
        return ResponseEntity.ok().body(response);
    }
}

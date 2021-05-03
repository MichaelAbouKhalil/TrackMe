package com.trackme.userservice.controller;

import com.trackme.models.common.CommonResponse;
import com.trackme.models.payload.request.profile.ProfileRequest;
import com.trackme.models.security.permission.AuthenticatedPermission;
import com.trackme.userservice.service.ProfileService;
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
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/profile")
    @AuthenticatedPermission
    public ResponseEntity<CommonResponse> getProfile() {
        log.info("received request on getProfile()");
        CommonResponse response = profileService.getCompleteUserProfile();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/profile")
    @AuthenticatedPermission
    public ResponseEntity<CommonResponse> saveUpdateProfile(@Valid @RequestBody ProfileRequest request) {
        log.info("received request on saveUpdateProfile()");
        CommonResponse response = profileService.saveUpdateProfile(request);
        return ResponseEntity.ok().body(response);
    }

}

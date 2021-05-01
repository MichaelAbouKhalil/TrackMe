package com.trackme.authservice.controller;

import com.trackme.authservice.service.UserService;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.payload.request.retrieveuser.RetrieveUserRequest;
import com.trackme.models.security.UserEntity;
import com.trackme.models.security.permission.GetUserPermission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/user")
    @GetUserPermission
    public ResponseEntity<CommonResponse> retrieveUser(@RequestBody RetrieveUserRequest request) {

        UserEntity user = userService.findUserByUsername(request);

        CommonResponse response = CommonResponse.ok(user);

        return ResponseEntity.ok().body(response);
    }
}

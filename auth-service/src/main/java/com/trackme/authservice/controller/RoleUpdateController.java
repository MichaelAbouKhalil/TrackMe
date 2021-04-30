package com.trackme.authservice.controller;

import com.trackme.authservice.service.RoleUpdateService;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.enums.RoleEnum;
import com.trackme.models.payload.request.roleupdate.RoleUpdateRequest;
import com.trackme.models.security.permission.PmPromotePermission;
import com.trackme.models.security.permission.PromotePermission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RoleUpdateController {

    private final RoleUpdateService roleUpdateService;

    @PostMapping("/role/promote")
    @PromotePermission
    public ResponseEntity<CommonResponse> promote(@Valid @RequestBody RoleUpdateRequest request){
        CommonResponse response = roleUpdateService.updateRole(request, true);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/role/promote/pm")
    @PmPromotePermission
    public ResponseEntity<CommonResponse> promotePm(@Valid @RequestBody RoleUpdateRequest request){
        CommonResponse response = roleUpdateService.updateRole(request, true);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/role/demote")
    @PromotePermission
    public ResponseEntity<CommonResponse> demote(@Valid @RequestBody RoleUpdateRequest request){
        CommonResponse response = roleUpdateService.updateRole(request, false);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/role/demote/pm")
    @PmPromotePermission
    public ResponseEntity<CommonResponse> demotePm(@Valid @RequestBody RoleUpdateRequest request){
        CommonResponse response = roleUpdateService.updateRole(request, false);
        return ResponseEntity.ok().body(response);
    }
}

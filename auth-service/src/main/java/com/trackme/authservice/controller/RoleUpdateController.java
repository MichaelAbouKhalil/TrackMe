package com.trackme.authservice.controller;

import com.trackme.authservice.service.RoleService;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.enums.RoleEnum;
import com.trackme.models.payload.request.roleupdate.RoleUpdateRequest;
import com.trackme.models.security.permission.PmPromotePermission;
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

    private final RoleService roleService;

    @PostMapping("/role/promote")
    public ResponseEntity<CommonResponse> promote(@Valid @RequestBody RoleUpdateRequest request){
        CommonResponse response = roleService.promote(request, null);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/role/promote/pm")
    @PmPromotePermission
    public ResponseEntity<CommonResponse> promotePm(@Valid @RequestBody RoleUpdateRequest request){
        CommonResponse response = roleService.promote(request, RoleEnum.PM);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/role/demote")
    public ResponseEntity<CommonResponse> demote(@Valid @RequestBody RoleUpdateRequest request){

        return null;
    }

    @PostMapping("/role/demote/pm")
    public ResponseEntity<CommonResponse> demotePm(@Valid @RequestBody RoleUpdateRequest request){

        return null;
    }
}

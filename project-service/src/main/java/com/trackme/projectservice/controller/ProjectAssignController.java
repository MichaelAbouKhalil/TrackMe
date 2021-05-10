package com.trackme.projectservice.controller;

import com.trackme.models.common.CommonResponse;
import com.trackme.models.payload.request.project.AssignRemoveRequest;
import com.trackme.models.project.ProjectEntity;
import com.trackme.models.security.permission.AdminPmPermission;
import com.trackme.models.security.permission.AuthenticatedPermission;
import com.trackme.projectservice.service.ProjectAssignService;
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
public class ProjectAssignController {

    private final ProjectAssignService projectAssignService;

    @PostMapping("/project/assign")
    @AuthenticatedPermission
    public ResponseEntity<CommonResponse<ProjectEntity>> assignToProject(@Valid @RequestBody AssignRemoveRequest request){
        log.info("received request on assignToProject()");
        CommonResponse<ProjectEntity> response = projectAssignService.assign(request);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/project/remove")
    @AdminPmPermission
    public ResponseEntity<CommonResponse> removeToProject(@Valid @RequestBody AssignRemoveRequest request){
        log.info("received request on removeToProject()");
        CommonResponse response = projectAssignService.remove(request);

        return ResponseEntity.ok().body(response);
    }
}

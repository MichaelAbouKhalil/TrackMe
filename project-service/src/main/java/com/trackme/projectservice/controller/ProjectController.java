package com.trackme.projectservice.controller;

import com.trackme.models.common.CommonResponse;
import com.trackme.models.payload.request.project.DeleteProjectRequest;
import com.trackme.models.payload.request.project.GetProjectRequest;
import com.trackme.models.payload.request.project.NewProjectRequest;
import com.trackme.models.payload.request.project.UpdateProjectRequest;
import com.trackme.models.security.permission.AdminPmPermission;
import com.trackme.models.security.permission.PmDevCustPermission;
import com.trackme.models.security.permission.PmPermission;
import com.trackme.projectservice.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/project")
    @PmDevCustPermission
    public ResponseEntity<CommonResponse<?>> getProject(@Valid @RequestBody GetProjectRequest request) {
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/project")
    @PmPermission
    public ResponseEntity<CommonResponse<?>> createProject(@Valid @RequestBody NewProjectRequest request) {
        return ResponseEntity.ok().body(null);
    }

    @PutMapping("/project")
    @PmPermission
    public ResponseEntity<CommonResponse<?>> updateProject(@Valid @RequestBody UpdateProjectRequest request) {
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping("/project")
    @AdminPmPermission
    public ResponseEntity<CommonResponse<?>> deleteProject(@Valid @RequestBody DeleteProjectRequest request) {
        return ResponseEntity.ok().body(null);
    }
}

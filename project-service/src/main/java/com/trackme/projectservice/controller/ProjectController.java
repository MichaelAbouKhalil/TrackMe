package com.trackme.projectservice.controller;

import com.trackme.models.common.CommonResponse;
import com.trackme.models.payload.request.project.DeleteProjectRequest;
import com.trackme.models.payload.request.project.GetProjectRequest;
import com.trackme.models.payload.request.project.NewProjectRequest;
import com.trackme.models.payload.request.project.UpdateProjectRequest;
import com.trackme.models.project.ProjectEntity;
import com.trackme.models.security.permission.AdminPmPermission;
import com.trackme.models.security.permission.AuthenticatedPermission;
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
    @AuthenticatedPermission
    public ResponseEntity<CommonResponse<?>> getProject(@Valid @RequestBody GetProjectRequest request) {
        log.info("received request on getProject()");
        CommonResponse<?> project = projectService.getProject(request);
        return ResponseEntity.ok().body(project);
    }

    @PostMapping("/project")
    @AdminPmPermission
    public ResponseEntity<CommonResponse<?>> createProject(@Valid @RequestBody NewProjectRequest request) {
        log.info("received request on createProject()");
        CommonResponse<?> project = projectService.createProject(request);
        return ResponseEntity.ok().body(project);
    }

    @PutMapping("/project")
    @AdminPmPermission
    public ResponseEntity<CommonResponse<?>> updateProject(@Valid @RequestBody UpdateProjectRequest request) {
        log.info("received request on updateProject()");
        CommonResponse response = projectService.updateProject(request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/project")
    @AdminPmPermission
    public ResponseEntity<CommonResponse<?>> deleteProject(@Valid @RequestBody DeleteProjectRequest request) {
        log.info("received request on deleteProject()");
        CommonResponse response = projectService.deleteProject(request);
        return ResponseEntity.ok().body(response);
    }
}

package com.trackme.projectservice.service;

import com.trackme.common.proxy.auth.AuthServiceFeignProxy;
import com.trackme.common.service.UserService;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.payload.request.project.AssignRemoveRequest;
import com.trackme.models.payload.request.retrieveuser.GetUserDetailsRequest;
import com.trackme.models.project.ProjectEntity;
import com.trackme.models.security.UserEntity;
import com.trackme.projectservice.utils.ProjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectAssignService {

    private final ProjectDbService projectDbService;
    private final UserService userService;

    public CommonResponse<ProjectEntity> assign(AssignRemoveRequest request) {

        ProjectEntity project = projectDbService.findProject(request.getProjectId());

        UserEntity user = userService.getUserDetails(GetUserDetailsRequest.builder()
                .email(request.getEmail()).build());

        project = ProjectUtils.assign(project, user);

        projectDbService.saveProject(project);

        return CommonResponse.ok(project);
    }

    public CommonResponse<ProjectEntity> remove(AssignRemoveRequest request) {

        ProjectEntity project = projectDbService.findProject(request.getProjectId());

        UserEntity user = userService.getUserDetails(GetUserDetailsRequest.builder()
                .email(request.getEmail()).build());

        project = ProjectUtils.remove(project, user);

        projectDbService.saveProject(project);

        return CommonResponse.ok(project);
    }
}

package com.trackme.projectservice.service;

import com.trackme.common.service.UserService;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.payload.request.project.DeleteProjectRequest;
import com.trackme.models.payload.request.project.GetProjectRequest;
import com.trackme.models.payload.request.project.NewProjectRequest;
import com.trackme.models.payload.request.project.UpdateProjectRequest;
import com.trackme.models.project.ProjectEntity;
import com.trackme.models.security.UserEntity;
import com.trackme.projectservice.utils.ProjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {

    private final ProjectDbService projectDbService;
    private final UserService userService;

    public CommonResponse createProject(NewProjectRequest request) {
        UserEntity user = userService.getUser();

        ProjectEntity project = ProjectUtils.buildProject(request, user);

        ProjectEntity savedProject = projectDbService.saveProject(project);

        return CommonResponse.ok(savedProject);
    }

    public CommonResponse getProject(GetProjectRequest request) {

        UserEntity user = userService.getUser();

        ProjectEntity project = projectDbService.findProject(request.getProjectId());

        ProjectUtils.checkOrgConstraint(project, user);

        return CommonResponse.ok(project);
    }

    public CommonResponse updateProject(UpdateProjectRequest request) {
        UserEntity user = userService.getUser();

        ProjectEntity project = projectDbService.findProject(request.getProjectId());

        ProjectUtils.checkOrgConstraint(project, user);

        ProjectEntity updatedProject = ProjectUtils.updateProject(request, project);

        projectDbService.saveProject(updatedProject);

        return CommonResponse.ok(updatedProject);
    }

    public CommonResponse deleteProject(DeleteProjectRequest request) {

        UserEntity user = userService.getUser();

        ProjectEntity project = projectDbService.findProject(request.getProjectId());

        ProjectUtils.checkOrgConstraint(project, user);

        projectDbService.deleteProject(project);

        return CommonResponse.ok();
    }

}

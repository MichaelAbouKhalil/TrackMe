package com.trackme.common.service;

import com.trackme.common.proxy.project.ProjectServiceFeignProxy;
import com.trackme.common.utils.ApiUtils;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.payload.request.project.GetProjectRequest;
import com.trackme.models.project.ProjectEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectProxyService {

    private final ProjectServiceFeignProxy projectServiceFeignProxy;

    public ProjectEntity getProject(Long projectId){
        log.info("finding project info for project id [{}]", projectId);

        ResponseEntity<CommonResponse<ProjectEntity>> response =
                projectServiceFeignProxy.getProject(projectId);

        ProjectEntity project = ApiUtils.getProjectFromResponseEntity(response);

        return project;
    }
}

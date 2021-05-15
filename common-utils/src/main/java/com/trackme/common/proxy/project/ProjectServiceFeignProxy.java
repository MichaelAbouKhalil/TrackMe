package com.trackme.common.proxy.project;

import com.trackme.common.interceptor.HeaderRequestInterceptor;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.payload.request.project.GetProjectRequest;
import com.trackme.models.payload.request.retrieveuser.GetUserDetailsRequest;
import com.trackme.models.project.ProjectEntity;
import com.trackme.models.security.UserEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "project-service",
        configuration = {HeaderRequestInterceptor.class}
)
public interface ProjectServiceFeignProxy {

    @GetMapping("/project/{id}")
    public ResponseEntity<CommonResponse<ProjectEntity>> getProject(@PathVariable("id") Long id);
}


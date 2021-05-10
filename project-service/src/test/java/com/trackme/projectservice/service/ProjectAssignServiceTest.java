package com.trackme.projectservice.service;

import com.trackme.common.service.UserService;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.enums.RoleEnum;
import com.trackme.models.payload.request.project.AssignRemoveRequest;
import com.trackme.models.payload.request.retrieveuser.GetUserDetailsRequest;
import com.trackme.models.project.AssignedPmEntity;
import com.trackme.models.project.ProjectEntity;
import com.trackme.models.security.RoleEntity;
import com.trackme.models.security.UserEntity;
import com.trackme.projectservice.Base;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ProjectAssignServiceTest extends Base {

    @Autowired
    ProjectAssignService projectAssignService;

    @MockBean
    ProjectDbService projectDbService;

    @MockBean
    UserService userService;

    AssignRemoveRequest assignRequest;
    AssignRemoveRequest removeRequest;
    ProjectEntity project;
    UserEntity assignUser;
    UserEntity removeUser;

    @BeforeEach
    void setUp() {
        assignUser = UserEntity.builder()
                .email("test@assign.com")
                .role(RoleEntity.builder()
                        .roleName(RoleEnum.PM.getRoleName()).build())
                .build();
        removeUser = UserEntity.builder()
                .email("test@remove.com")
                .role(RoleEntity.builder()
                        .roleName(RoleEnum.PM.getRoleName()).build())
                .build();
        project = ProjectEntity.builder()
                .id(1L).assignedPm(AssignedPmEntity.builder()
                .email(removeUser.getEmail()).build()).build();

        assignRequest = AssignRemoveRequest.builder()
                .projectId(1L).email(assignUser.getEmail()).build();
        removeRequest = AssignRemoveRequest.builder()
                .projectId(1L).email(removeUser.getEmail()).build();

    }

    @DisplayName("Assign Tests")
    @Nested
    class AssignTests{
        @Test
        public void assign_Valid() {
            when(projectDbService.findProject(any(Long.class)))
                    .thenReturn(project);
            when((userService.getUserDetails(any(GetUserDetailsRequest.class))))
                    .thenReturn(assignUser);
            when(projectDbService.saveProject(any(ProjectEntity.class)))
                    .thenReturn(project);

            int initialSize = project.getAssignedPms().size();

            CommonResponse<ProjectEntity> response = projectAssignService.assign(assignRequest);

            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertTrue(response.isSuccess());
            assertEquals(
                    initialSize + 1,
                    response.getPayload().getAssignedPms().size()
            );
        }
    }

    @DisplayName("Remove Tests")
    @Nested
    class RemoveTests{
        @Test
        public void remove_Valid() {
            when(projectDbService.findProject(any(Long.class)))
                    .thenReturn(project);
            when((userService.getUserDetails(any(GetUserDetailsRequest.class))))
                    .thenReturn(removeUser);
            when(projectDbService.saveProject(any(ProjectEntity.class)))
                    .thenReturn(project);

            int initialSize = project.getAssignedPms().size();

            CommonResponse<ProjectEntity> response = projectAssignService.remove(removeRequest);

            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertTrue(response.isSuccess());
            assertEquals(
                    initialSize - 1,
                    response.getPayload().getAssignedPms().size()
            );
        }
    }
}
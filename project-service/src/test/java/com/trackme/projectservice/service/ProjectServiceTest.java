package com.trackme.projectservice.service;

import com.trackme.common.service.UserService;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.enums.ProjectStatusEnum;
import com.trackme.models.enums.RoleEnum;
import com.trackme.models.exception.InvalidOperationException;
import com.trackme.models.exception.ProjectNotFoundException;
import com.trackme.models.payload.request.project.DeleteProjectRequest;
import com.trackme.models.payload.request.project.GetProjectRequest;
import com.trackme.models.payload.request.project.NewProjectRequest;
import com.trackme.models.payload.request.project.UpdateProjectRequest;
import com.trackme.models.project.AssignedPmEntity;
import com.trackme.models.project.ProjectEntity;
import com.trackme.models.security.RoleEntity;
import com.trackme.models.security.UserEntity;
import com.trackme.projectservice.Base;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProjectServiceTest extends Base {

    @Autowired
    ProjectService projectService;

    @MockBean
    ProjectDbService projectDbService;

    @MockBean
    UserService userService;

    ProjectEntity project;
    UserEntity user;

    NewProjectRequest newProjectRequest;
    GetProjectRequest getProjectRequest;
    UpdateProjectRequest updateProjectRequest;
    DeleteProjectRequest deleteProjectRequest;

    @BeforeEach
    void setUp() {
        user = UserEntity.builder()
                .username("test-pm")
                .email("pm@test.com")
                .orgId("test-org")
                .role(RoleEntity.builder()
                        .roleName(RoleEnum.PM.getRoleName()).build())
                .build();

        project = ProjectEntity.builder()
                .id(1L).name("test-proj")
                .ordId("test-org")
                .status(ProjectStatusEnum.CLOSED_PROJECT.getName())
                .assignedPm(AssignedPmEntity.builder()
                        .email(user.getEmail()).build())
                .build();

        newProjectRequest = NewProjectRequest.builder().name(project.getName()).build();
        getProjectRequest = GetProjectRequest.builder().projectId(project.getId()).build();
        deleteProjectRequest = DeleteProjectRequest.builder().projectId(project.getId()).build();
        updateProjectRequest = UpdateProjectRequest.builder()
                .projectId(project.getId())
                .description(project.getDescription())
                .status(project.getStatus())
                .build();
    }

    @DisplayName("Create Project Tests")
    @Nested
    class CreateProjectTests {
        @Test
        public void createProject_Valid() {
            when(userService.getUser()).thenReturn(user);
            when(projectDbService.saveProject(any(ProjectEntity.class)))
                    .thenReturn(project);

            CommonResponse response = projectService.createProject(newProjectRequest);

            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertEquals(Boolean.TRUE, response.isSuccess());
            assertEquals(project, response.getPayload());
        }

        @Test
        public void createProject_UserServiceException_Invalid() {
            when(userService.getUser()).thenThrow(new UsernameNotFoundException(""));
            when(projectDbService.saveProject(any(ProjectEntity.class)))
                    .thenReturn(project);

            assertThrows(UsernameNotFoundException.class,
                    () -> {
                        projectService.createProject(newProjectRequest);
                    });

            verify(projectDbService, times(0));
        }

        @Test
        public void createProject_ProjectDbServiceException_Invalid() {
            when(userService.getUser()).thenReturn(user);
            when(projectDbService.saveProject(any(ProjectEntity.class)))
                    .thenThrow(new RuntimeException(""));

            assertThrows(RuntimeException.class,
                    () -> {
                        projectService.createProject(newProjectRequest);
                    });
        }
    }

    @DisplayName("Get Project Tests")
    @Nested
    class GetProjectTests {
        @Test
        public void getProject_Valid() {
            when(userService.getUser()).thenReturn(user);
            when(projectDbService.findProject(any(Long.class)))
                    .thenReturn(project);

            CommonResponse response =
                    projectService.getProject(getProjectRequest);

            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertTrue(response.isSuccess());
            assertEquals(project, response.getPayload());
        }

        @Test
        public void getProject_UserServiceException_Invalid() {
            when(userService.getUser()).thenThrow(new UsernameNotFoundException(""));
            when(projectDbService.findProject(any(Long.class)))
                    .thenReturn(project);

            assertThrows(UsernameNotFoundException.class,
                    () -> {
                        projectService.getProject(getProjectRequest);
                    }
            );

            verify(projectDbService, times(0));
        }

        @Test
        public void getProject_ProjectDbServiceException_Invalid() {
            when(userService.getUser()).thenReturn(user);
            when(projectDbService.saveProject(any(ProjectEntity.class)))
                    .thenThrow(new ProjectNotFoundException(""));

            assertThrows(ProjectNotFoundException.class,
                    () -> {
                        projectService.getProject(getProjectRequest);
                    }
            );
        }

        @Test
        public void getProject_DifferentOrg_Invalid() {
            when(userService.getUser()).thenReturn(user);
            project.setOrdId("org");
            when(projectDbService.findProject(any(Long.class)))
                    .thenReturn(project);

            assertThrows(InvalidOperationException.class,
                    () -> {projectService.getProject(getProjectRequest);});
        }

        @Test
        public void getProject_DifferentOrg_Admin_Valid() {
            user.setRoles(Arrays.asList(
                    RoleEntity.builder().roleName(
                            RoleEnum.ADMIN.getRoleName()).build()));
            user.setOrgId(null);

            when(userService.getUser()).thenReturn(user);
            when(projectDbService.findProject(any(Long.class)))
                    .thenReturn(project);

            CommonResponse response = projectService.getProject(getProjectRequest);

            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertTrue(response.isSuccess());
            assertEquals(ProjectServiceTest.this.project, response.getPayload());
        }
    }

}
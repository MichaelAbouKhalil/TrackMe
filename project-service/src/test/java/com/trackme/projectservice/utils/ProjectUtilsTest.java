package com.trackme.projectservice.utils;

import com.trackme.models.enums.ProjectStatusEnum;
import com.trackme.models.enums.RoleEnum;
import com.trackme.models.exception.InvalidOperationException;
import com.trackme.models.payload.request.project.NewProjectRequest;
import com.trackme.models.payload.request.project.UpdateProjectRequest;
import com.trackme.models.project.ProjectEntity;
import com.trackme.models.security.RoleEntity;
import com.trackme.models.security.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProjectUtilsTest {

    NewProjectRequest newProjectRequest;
    UpdateProjectRequest updateProjectRequest;

    ProjectEntity project;
    UserEntity user;
    UserEntity admin;

    @BeforeEach
    void setUp() {
        newProjectRequest = NewProjectRequest.builder()
                .name("proj").description("desc")
                .build();
        updateProjectRequest = UpdateProjectRequest.builder()
                .projectId(1L).newName("new-proj")
                .description("new-desc")
                .status(ProjectStatusEnum.CLOSED_PROJECT.getName())
                .build();

        project = ProjectEntity.builder()
                .name("proj").ordId("org")
                .status(ProjectStatusEnum.OPEN_PROJECT.getName())
                .id(1L).startDate(LocalDateTime.now())
                .build();

        user = UserEntity.builder()
                .username("user").email("user@test.com")
                .orgId("org")
                .role(RoleEntity.builder()
                        .roleName(RoleEnum.PM.getRoleName()).build())
                .build();

        admin = UserEntity.builder()
                .username("admin").email("admin@test.com")
                .role(RoleEntity.builder()
                        .roleName(RoleEnum.ADMIN.getRoleName()).build())
                .build();
    }

    @DisplayName("Build Project Tests")
    @Nested
    class BuildProjectTests{
        @Test
        public void buildProject_Valid() {
            ProjectEntity projectEntity = ProjectUtils.buildProject(newProjectRequest, user);

            assertEquals(newProjectRequest.getName(), projectEntity.getName());
            assertEquals(newProjectRequest.getDescription(), projectEntity.getDescription());
            assertEquals(ProjectStatusEnum.OPEN_PROJECT.getName(), projectEntity.getStatus());
            assertEquals(user.getEmail(), projectEntity.getAssignedPms().get(0).getEmail());
            assertEquals(user.getOrgId(), projectEntity.getOrdId());
        }
    }

    @DisplayName("Check Org Constraint Tests")
    @Nested
    class CheckOrgConstraintTests{
        @Test
        public void checkOrgConstraint_Admin_Valid() {
            assertDoesNotThrow(() -> {ProjectUtils.checkOrgConstraint(project, admin);});
        }
        @Test
        public void checkOrgConstraint_SameOrg_PM_Valid() {
            assertDoesNotThrow(() -> {ProjectUtils.checkOrgConstraint(project,user);});
        }
        @Test
        public void checkOrgConstraint_DifferentOrg_PM_Invalid() {
            user.setOrgId("123");
            assertThrows(InvalidOperationException.class,
                    () -> {ProjectUtils.checkOrgConstraint(project, user);});
        }
    }

    @DisplayName("Update Project Tests")
    @Nested
    class UpdateProjectTests{
        @Test
        public void updateProject_Valid() {
            ProjectEntity updatedProject = ProjectUtils.updateProject(updateProjectRequest, project);

            assertEquals(updateProjectRequest.getNewName(), updatedProject.getName());
            assertEquals(updateProjectRequest.getDescription(), updatedProject.getDescription());
            assertEquals(updateProjectRequest.getStatus(), updatedProject.getStatus());
            assertNotNull(updatedProject.getEndDate());
        }
    }
}
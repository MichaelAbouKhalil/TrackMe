package com.trackme.projectservice.utils;

import com.trackme.models.enums.ProjectStatusEnum;
import com.trackme.models.enums.RoleEnum;
import com.trackme.models.exception.InvalidOperationException;
import com.trackme.models.payload.request.project.NewProjectRequest;
import com.trackme.models.payload.request.project.UpdateProjectRequest;
import com.trackme.models.project.AssignedCustEntity;
import com.trackme.models.project.AssignedDevEntity;
import com.trackme.models.project.AssignedPmEntity;
import com.trackme.models.project.ProjectEntity;
import com.trackme.models.security.RoleEntity;
import com.trackme.models.security.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;

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
                .assignedPm(AssignedPmEntity
                        .builder().email("pm@initial.com").build())
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
    class BuildProjectTests {
        @Test
        public void buildProject_Valid() {
            ProjectEntity projectEntity = ProjectUtils.buildProject(newProjectRequest, user);

            assertEquals(newProjectRequest.getName(), projectEntity.getName());
            assertEquals(newProjectRequest.getDescription(), projectEntity.getDescription());
            assertEquals(ProjectStatusEnum.OPEN_PROJECT.getName(), projectEntity.getStatus());
            assertEquals(user.getEmail(), projectEntity.getAssignedPms().stream().findFirst().get().getEmail());
            assertEquals(user.getOrgId(), projectEntity.getOrdId());
        }
    }

    @DisplayName("Check Org Constraint Tests")
    @Nested
    class CheckOrgConstraintTests {
        @Test
        public void checkOrgConstraint_Admin_Valid() {
            assertDoesNotThrow(() -> {
                ProjectUtils.checkOrgConstraint(project, admin);
            });
        }

        @Test
        public void checkOrgConstraint_SameOrg_PM_Valid() {
            assertDoesNotThrow(() -> {
                ProjectUtils.checkOrgConstraint(project, user);
            });
        }

        @Test
        public void checkOrgConstraint_DifferentOrg_PM_Invalid() {
            user.setOrgId("123");
            assertThrows(InvalidOperationException.class,
                    () -> {
                        ProjectUtils.checkOrgConstraint(project, user);
                    });
        }
    }

    @DisplayName("Update Project Tests")
    @Nested
    class UpdateProjectTests {
        @Test
        public void updateProject_Valid() {
            ProjectEntity updatedProject = ProjectUtils.updateProject(updateProjectRequest, project);

            assertEquals(updateProjectRequest.getNewName(), updatedProject.getName());
            assertEquals(updateProjectRequest.getDescription(), updatedProject.getDescription());
            assertEquals(updateProjectRequest.getStatus(), updatedProject.getStatus());
            assertNotNull(updatedProject.getEndDate());
        }
    }


    @DisplayName("Assign User Tests")
    @Nested
    class AssignUserTests {
        @Test
        public void assign_PM_Valid() {
            int initialSize = project.getAssignedPms().size();
            project = ProjectUtils.assign(project, user);

            assertEquals(initialSize + 1, project.getAssignedPms().size());
        }

        @Test
        public void assign_Dev_Valid() {
            int initialSize = project.getAssignedDevs().size();
            user.setRoles(Arrays.asList(RoleEntity.builder()
                    .roleName(RoleEnum.DEV.getRoleName()).build()));

            project = ProjectUtils.assign(project, user);

            assertEquals(initialSize + 1, project.getAssignedDevs().size());
        }

        @Test
        public void assign_Cust_Valid() {
            int initialSize = project.getAssignedDevs().size();
            user.setRoles(Arrays.asList(RoleEntity.builder()
                    .roleName(RoleEnum.CUSTOMER.getRoleName()).build()));

            project = ProjectUtils.assign(project, user);

            assertEquals(initialSize + 1, project.getAssignedCustomers().size());
        }

        @Test
        public void assign_Admin_Invalid() {
            user.setRoles(Arrays.asList(RoleEntity.builder()
                    .roleName(RoleEnum.ADMIN.getRoleName()).build()));

            assertThrows(InvalidOperationException.class,
                    () -> {
                        ProjectUtils.assign(project, user);
                    });

        }

        @Test
        public void assign_AddDuplicate_DoesntAdd_Valid() {
            int initialSize = project.getAssignedPms().size();
            project = ProjectUtils.assign(project, user);

            assertEquals(initialSize + 1, project.getAssignedPms().size());

            project = ProjectUtils.assign(project, user);

            // should still be initial size +1 because second attempt at adding will not add
            assertEquals(initialSize + 1, project.getAssignedPms().size());


        }
    }

    @DisplayName("Remove User Tests")
    @Nested
    class RemoveUserTests {
        UserEntity pm, dev, cust;

        @BeforeEach
        void setUp() {

            pm = UserEntity.builder().email("pm@email.com")
                    .role(RoleEntity.builder().roleName(
                            RoleEnum.PM.getRoleName()).build()).build();

            dev = UserEntity.builder().email("dev@email.com")
                    .role(RoleEntity.builder().roleName(
                            RoleEnum.DEV.getRoleName()).build()).build();

            cust = UserEntity.builder().email("cust@email.com")
                    .role(RoleEntity.builder().roleName(
                            RoleEnum.CUSTOMER.getRoleName()).build()).build();

            project.setAssignedPms(
                    Set.of(AssignedPmEntity.builder().email(pm.getEmail())
                            .build())
            );
            project.setAssignedDevs(
                    Set.of(AssignedDevEntity.builder().email(dev.getEmail())
                            .build())
            );
            project.setAssignedCustomers(
                    Set.of(AssignedCustEntity.builder().email(cust.getEmail())
                            .build())
            );
        }

        @Test
        public void remove_Pm_Valid() {
            int initialSize = project.getAssignedPms().size();

            project = ProjectUtils.remove(project, pm);

            assertEquals(initialSize - 1, project.getAssignedPms().size());
        }

        @Test
        public void remove_Dev_Valid() {
            int initialSize = project.getAssignedDevs().size();

            project = ProjectUtils.remove(project, dev);

            assertEquals(initialSize - 1, project.getAssignedDevs().size());
        }

        @Test
        public void remove_Cust_Valid() {
            int initialSize = project.getAssignedCustomers().size();

            project = ProjectUtils.remove(project, cust);

            assertEquals(initialSize - 1, project.getAssignedCustomers().size());
        }

        @Test
        public void remove_Admin_Invalid() {
            user.setRoles(Arrays.asList(RoleEntity.builder().roleName(RoleEnum.ADMIN.getRoleName()).build()));
            assertThrows(InvalidOperationException.class,
                    () -> {
                        ProjectUtils.remove(project, user);
                    });

        }

        @Test
        public void remove_NotExisting_User_Valid() {
            int initialSize = project.getAssignedCustomers().size();

            project = ProjectUtils.remove(project, user);

            assertEquals(initialSize, project.getAssignedCustomers().size());

        }
    }
}


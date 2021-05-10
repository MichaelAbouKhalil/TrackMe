package com.trackme.projectservice.utils;

import com.trackme.common.utils.OrgUtils;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDateTime;
import java.util.Iterator;

@Slf4j
public class ProjectUtils {

    /**
     * build new project from request and user entity
     *
     * @param request
     * @param user
     * @return
     */
    public static ProjectEntity buildProject(NewProjectRequest request, UserEntity user) {
        return ProjectEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .status(ProjectStatusEnum.OPEN_PROJECT.getName())
                .ordId(user.getOrgId())
                .assignedPm(AssignedPmEntity.builder()
                        .email(user.getEmail()).build())
                .build();
    }

    /**
     * check if user org id is the same for project org id
     *
     * @param project
     * @param user
     */
    public static void checkOrgConstraint(ProjectEntity project, UserEntity user) {
        if (RoleEnum.ADMIN.getRoleName().equals(
                user.getRoles().get(0).getRoleName())) {
            log.info("user is admin will not check correlation with org id");
            return;
        }

        log.info("checking if user has same org as project");
        if (!project.getOrdId().equals(user.getOrgId())) {
            log.error("user and project have different org id, this request is rejected");
            throw new InvalidOperationException("user and project have different org id.");
        }
    }

    /**
     * update project's name or description or status
     *
     * @param request
     * @param project
     * @return
     */
    public static ProjectEntity updateProject(UpdateProjectRequest request, ProjectEntity project) {

        if (!StringUtils.isEmpty(request.getNewName())) {
            project.setName(request.getNewName());
        }

        if (!StringUtils.isEmpty(request.getDescription())) {
            project.setDescription(request.getDescription());
        }

        if (!StringUtils.isEmpty(request.getStatus())) {
            ProjectStatusEnum newStatus = ProjectStatusEnum.findByName(request.getStatus());

            if (newStatus.equals(ProjectStatusEnum.CLOSED_PROJECT)) {
                project.setEndDate(LocalDateTime.now());
            } else if (newStatus.equals(ProjectStatusEnum.OPEN_PROJECT)) {
                project.setEndDate(null);
            }
            project.setStatus(request.getStatus());
        }

        return project;
    }

    /**
     * assign pm, dev, customer to project
     *
     * @param project
     * @param user
     * @return
     */
    public static ProjectEntity assign(ProjectEntity project, UserEntity user) {

        OrgUtils.orgConstraintForAssignRemove(project, user);

        RoleEntity userRole = user.getRoles().get(0);
        String userEmail = user.getEmail();

        if (RoleEnum.PM.getRoleName().equals(userRole.getRoleName())) {
            project.getAssignedPms().add(
                    AssignedPmEntity.builder().email(userEmail).build());
        } else if (RoleEnum.DEV.getRoleName().equals(userRole.getRoleName())) {
            project.getAssignedDevs().add(
                    AssignedDevEntity.builder().email(userEmail).build());
        } else if (RoleEnum.CUSTOMER.getRoleName().equals(userRole.getRoleName())) {
            project.getAssignedCustomers().add(
                    AssignedCustEntity.builder().email(userEmail).build());
        }

        return project;
    }

    /**
     * remove pm, dev, customer from project
     *
     * @param project
     * @param user
     * @return
     */
    public static ProjectEntity remove(ProjectEntity project, UserEntity user) {
        OrgUtils.orgConstraintForAssignRemove(project, user);

        RoleEntity userRole = user.getRoles().get(0);
        String userEmail = user.getEmail();

        if (RoleEnum.PM.getRoleName().equals(userRole.getRoleName())) {
            Iterator<AssignedPmEntity> it = project.getAssignedPms().iterator();
            while (it.hasNext()) {
                AssignedPmEntity i = it.next();
                if (i.getEmail().equals(userEmail)) {
                    it.remove();
                }
            }
        } else if (RoleEnum.DEV.getRoleName().equals(userRole.getRoleName())) {
            Iterator<AssignedDevEntity> it = project.getAssignedDevs().iterator();
            while (it.hasNext()) {
                AssignedDevEntity i = it.next();
                if (i.getEmail().equals(userEmail)) {
                    it.remove();
                }
            }
        } else if (RoleEnum.CUSTOMER.getRoleName().equals(userRole.getRoleName())) {
            Iterator<AssignedCustEntity> it = project.getAssignedCustomers().iterator();
            while (it.hasNext()) {
                AssignedCustEntity i = it.next();
                if (i.getEmail().equals(userEmail)) {
                    it.remove();
                }
            }
        }

        return project;
    }
}

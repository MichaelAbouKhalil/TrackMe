package com.trackme.projectservice.utils;

import com.trackme.common.utils.OrgUtils;
import com.trackme.models.enums.ProjectStatusEnum;
import com.trackme.models.enums.RoleEnum;
import com.trackme.models.exception.InvalidOperationException;
import com.trackme.models.payload.request.project.NewProjectRequest;
import com.trackme.models.payload.request.project.UpdateProjectRequest;
import com.trackme.models.project.*;
import com.trackme.models.security.RoleEntity;
import com.trackme.models.security.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
        ProjectEntity project = ProjectEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .status(ProjectStatusEnum.OPEN_PROJECT.getName())
                .ordId(user.getOrgId())
                .build();

        AssignedPmEntity assignedPm = AssignedPmEntity.builder()
                .email(user.getEmail()).project(project).build();

        project = assign(project, user);

        return project;
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
            Set<AssignedPmEntity> assignedPms = new HashSet<>(project.getAssignedPms());
            AssignedPmEntity assigned = AssignedPmEntity.builder().email(userEmail).project(project).build();
            assignedPms.add(assigned);
            project.setAssignedPms(assignedPms);
        } else if (RoleEnum.DEV.getRoleName().equals(userRole.getRoleName())) {
            Set<AssignedDevEntity> assignedDev = new HashSet<>(project.getAssignedDevs());
            AssignedDevEntity assigned = AssignedDevEntity.builder().email(userEmail).project(project).build();
            assignedDev.add(assigned);
            project.setAssignedDevs(assignedDev);
        } else if (RoleEnum.CUSTOMER.getRoleName().equals(userRole.getRoleName())) {
            Set<AssignedCustEntity> assignedCusts = new HashSet<>(project.getAssignedCustomers());
            AssignedCustEntity assigned = AssignedCustEntity.builder().email(userEmail).project(project).build();
            assignedCusts.add(assigned);
            project.setAssignedCustomers(assignedCusts);
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
            project.setAssignedPms((Set<AssignedPmEntity>) removeFromSet(project.getAssignedPms(),
                    userEmail));
        } else if (RoleEnum.DEV.getRoleName().equals(userRole.getRoleName())) {
            project.setAssignedDevs((Set<AssignedDevEntity>) removeFromSet(project.getAssignedDevs(),
                    userEmail));
        } else if (RoleEnum.CUSTOMER.getRoleName().equals(userRole.getRoleName())) {
            project.setAssignedCustomers((Set<AssignedCustEntity>) removeFromSet(project.getAssignedCustomers(),
                    userEmail));
        }

        return project;
    }

    private static Set<? extends AssignUser> removeFromSet(Set<? extends AssignUser> set, String email) {
        set = set.stream()
                .filter(o -> !o.getEmail().equals(email)) // keep the odds
                .collect(Collectors.toSet());     // collect to a new set
        return set;
    }
}

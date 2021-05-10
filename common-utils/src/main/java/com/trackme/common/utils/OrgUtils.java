package com.trackme.common.utils;

import com.trackme.models.enums.RoleEnum;
import com.trackme.models.exception.InvalidOperationException;
import com.trackme.models.project.ProjectEntity;
import com.trackme.models.security.UserEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrgUtils {

    public static void orgConstraintForAssignRemove(ProjectEntity project, UserEntity user){

        if(user.getRoles().get(0).getRoleName().equals(RoleEnum.ADMIN.getRoleName())){
            log.error("cannot assign admin to any project");
            throw new InvalidOperationException("cannot assign admin to any project");
        }
    }
}

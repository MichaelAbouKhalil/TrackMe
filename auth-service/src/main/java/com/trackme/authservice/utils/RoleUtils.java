package com.trackme.authservice.utils;

import com.trackme.models.enums.RoleEnum;
import com.trackme.models.security.RoleEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RoleUtils {

    /**
     * this method is used to determine if the role to be promoted is valid and has prefix _PENDING
     *
     * @param userRole
     * @param roleToPromote
     * @return
     */
    public static RoleEnum getPromoteRole(RoleEntity userRole, RoleEnum roleToPromote) {

        String userRoleName = userRole.getRoleName();

        if (roleToPromote == null) {

            if (userRoleName.equals(RoleEnum.DEV_PENDING.getRoleName())) {
                return RoleEnum.DEV;
            }

            if (userRoleName.equals(RoleEnum.CUSTOMER_PENDING.getRoleName())) {
                return RoleEnum.CUSTOMER;
            }
        } else if (roleToPromote.equals(RoleEnum.PM)) {
            if (!userRoleName.equals(RoleEnum.PM_PENDING.getRoleName())) {
                log.error("invalid promote operation, user cannot be promoted from [{}] to [{}]",
                        userRoleName, roleToPromote.getRoleName());
                return null;
            }

            return RoleEnum.PM;
        } else if (roleToPromote.equals(RoleEnum.ADMIN)) {
            log.error("invalid promote operation, user cannot be promoted to [{}]",
                    roleToPromote.getRoleName());
            return null;
        }

        log.error("invalid promote operation");
        return null;

    }
}

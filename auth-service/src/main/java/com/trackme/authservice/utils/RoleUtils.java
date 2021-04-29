package com.trackme.authservice.utils;

import com.trackme.models.constants.ConstantMessages;
import com.trackme.models.enums.RoleEnum;
import com.trackme.models.security.RoleEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RoleUtils {

    /**
     * this method is used to remove _PENDING suffix for promote and add for demote
     *
     * @param userRole
     * @return
     */
    public static RoleEnum getUpdateRole(RoleEntity userRole) {
        RoleEnum roleToPromote;
        String roleToPromoteName;
        String userRoleName = userRole.getRoleName();

        if (userRoleName.contains(ConstantMessages.PENDING_ROLE_SUFFIX)) {
            roleToPromoteName = userRoleName.replace(ConstantMessages.PENDING_ROLE_SUFFIX, "");
        } else {
            roleToPromoteName = userRoleName + ConstantMessages.PENDING_ROLE_SUFFIX;
        }

        roleToPromote = RoleEnum.returnByName(roleToPromoteName);

        return roleToPromote;
    }
}

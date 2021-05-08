package com.trackme.authservice.service;

import com.trackme.authservice.utils.OrgService;
import com.trackme.authservice.utils.RoleUtils;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.constants.ConstantMessages;
import com.trackme.models.enums.RoleEnum;
import com.trackme.models.exception.InvalidRoleException;
import com.trackme.models.payload.request.roleupdate.RoleUpdateRequest;
import com.trackme.models.security.RoleEntity;
import com.trackme.models.security.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleUpdateService {

    private final AuthUserService authUserService;
    private final RoleService roleService;
    private final OrgService orgService;

    public CommonResponse updateRole(RoleUpdateRequest request, boolean isPromote){

        // find user to promote
        UserEntity user = authUserService.findUserByEmail(request.getEmail());

        RoleEntity userRole = user.getRoles().get(0);

        // get role to promote
        RoleEnum promoteRole = RoleUtils.getUpdateRole(userRole, isPromote);

        if (promoteRole == null) {
            throw new InvalidRoleException("User has invalid Role for this request");
        }

        if (!(userRole.getRoleName().equals(RoleEnum.PM.getRoleName()))
                && !(userRole.getRoleName().equals(RoleEnum.PM_PENDING.getRoleName())) ) {
            // to promote/demote DEV and Customers, the PM should have same org ID
            // the admin can do it without having same org ID
            orgService.validateOrgPromoteDemote(user);
        }

        // get role entity from database
        RoleEntity dbRole = roleService.findRoleByRoleName(promoteRole.getRoleName());

        // update user in database
        UserEntity savedUser = authUserService.updateUserRoles(user, dbRole);

        if (savedUser == null) {
            return CommonResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ConstantMessages.USER_NOT_SAVED);
        }

        return CommonResponse.ok();
    }
}

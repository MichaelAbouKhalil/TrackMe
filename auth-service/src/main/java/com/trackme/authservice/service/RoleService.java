package com.trackme.authservice.service;

import com.trackme.authservice.repository.RoleRepository;
import com.trackme.authservice.repository.UserRepository;
import com.trackme.authservice.utils.OrgService;
import com.trackme.authservice.utils.RoleUtils;
import com.trackme.authservice.utils.SecurityUtils;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.constants.ConstantMessages;
import com.trackme.models.enums.RoleEnum;
import com.trackme.models.exception.InvalidOperationException;
import com.trackme.models.exception.InvalidRoleException;
import com.trackme.models.exception.RoleNotFoundException;
import com.trackme.models.payload.request.roleupdate.RoleUpdateRequest;
import com.trackme.models.security.RoleEntity;
import com.trackme.models.security.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OrgService orgService;

    public CommonResponse promote(RoleUpdateRequest request, RoleEnum roleToPromote) {

        // find user to promote
        log.info("finding user with email [{}]", request.getEmail());
        UserEntity user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new UsernameNotFoundException("User with email [" + request.getEmail() + "] is not found.")
        );

        // get role to promote
        RoleEnum promoteRole = RoleUtils.getPromoteRole(user.getRoles().get(0), roleToPromote);

        if (promoteRole == null) {
            throw new InvalidRoleException("User has invalid Role for this request");
        }

        if (!promoteRole.equals(RoleEnum.PM)) {
            // to promote DEV and Customers, the PM should have same org ID
            // the admin can do it without having same org ID
            orgService.validateOrg(user);
        }

        // get role entity from database
        RoleEntity dbRole = roleRepository.findByRoleName(promoteRole.getRoleName())
                .orElseThrow(() -> {
                    throw new RoleNotFoundException("Requested Role [" + promoteRole.getRoleName() + "] not found.");
                });

        // update user roles
//        user.getRoles().clear();
        user.setRoles(Arrays.asList(dbRole));

        log.info("promoted user [{}] with role [{}]", request.getEmail(), dbRole.getRoleName());
        log.info("updating user in database");

        // update user in database
        UserEntity savedUser = userRepository.save(user);

        if (savedUser == null) {
            return CommonResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ConstantMessages.USER_NOT_SAVED);
        }

        return CommonResponse.ok();
    }

}

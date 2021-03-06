package com.trackme.authservice.utils;

import com.trackme.authservice.repository.UserRepository;
import com.trackme.common.security.SecurityUtils;
import com.trackme.models.enums.RoleEnum;
import com.trackme.models.exception.InvalidOperationException;
import com.trackme.models.security.RoleEntity;
import com.trackme.models.security.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrgService {

    private final UserRepository userRepository;

    public void validateOrgPromoteDemote(UserEntity user) {
        String orgId = user.getOrgId();

        // get authenticated user
        UserEntity authenticatedUser = userRepository.findByUsername(SecurityUtils.getUsername()).orElseThrow();
        RoleEntity authUserRole = authenticatedUser.getRoles().get(0);

        log.info("checking if user [{}] have the authority to perform this action", authenticatedUser.getUsername());

        // if user is PM
        if (authUserRole.getRoleName().equals(RoleEnum.PM.getRoleName())) {

            // check org id equality
            String authUserOrgId = authenticatedUser.getOrgId();

            if (!authUserOrgId.equals(orgId)) {
                log.info("authenticated user [{}] and user [{}] to promote have different organization",
                        authenticatedUser.getUsername(), user.getUsername());
                throw new InvalidOperationException("Different org Id");
            }
            // if auth user is not pm or admin they can't promote
        } else if (!authUserRole.getRoleName().equals(RoleEnum.ADMIN.getRoleName())) {
            log.info("user [{}] is not admin or pm, this operation is invalid", authenticatedUser.getUsername());
            throw new InvalidOperationException("user [" + authenticatedUser.getUsername() + "] " +
                    "doesn't have authority to perform this action");
        }
    }

    public void checkSameOrg(UserEntity user, UserEntity authUser) {

        if (user.getRoles().get(0).getRoleName()
                .equals(RoleEnum.ADMIN.getRoleName())) {
            log.info("user [{}] is admin, not org constraint", user.getUsername());
            return;
        }

        if (authUser.getRoles().get(0).getRoleName()
                .equals(RoleEnum.ADMIN.getRoleName())) {
            log.info("auth user [{}] is admin, not org constraint", authUser.getUsername());
            return;
        }

        log.info("checking if auth user [{}] and user [{}] have same org", authUser.getUsername(), user.getUsername());
        if (!authUser.getOrgId().equals(user.getOrgId())) {
            throw new InvalidOperationException("auth user [" + authUser.getUsername() + "] cannot request user details for user [" +
                    user.getUsername() + "]");
        }
        log.info("auth user [{}] and user [{}] have same org", authUser.getUsername(), user.getUsername());
    }
}

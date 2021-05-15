package com.trackme.common.service;

import com.trackme.common.proxy.auth.AuthServiceFeignProxy;
import com.trackme.common.security.SecurityUtils;
import com.trackme.common.utils.ApiUtils;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.payload.request.retrieveuser.GetUserDetailsRequest;
import com.trackme.models.security.RoleEntity;
import com.trackme.models.security.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final AuthServiceFeignProxy authServiceFeignProxy;

    public UserEntity getUser() {
        List<RoleEntity> roles = new ArrayList<>();
        for (String s : SecurityUtils.getUserRoles()){
            roles.add(RoleEntity.builder().roleName(s).build());
        }

        return UserEntity.builder()
                .username(SecurityUtils.getUsername())
                .email(SecurityUtils.getUserEmail())
                .id(SecurityUtils.getUserId())
                .orgId(SecurityUtils.getUserOrgId())
                .roles(roles)
                .build();
    }

    public UserEntity getUserDetails(GetUserDetailsRequest request) {

        log.info("location user with username/email [{}] from auth service",
                StringUtils.isEmpty(request.getUsername()) ? request.getEmail() : request.getUsername());

        ResponseEntity<CommonResponse<UserEntity>> response = authServiceFeignProxy.getUserDetails(request);

        return ApiUtils.getUserFromResponseEntity(response);
    }

}

package com.trackme.userservice.service;

import com.trackme.common.security.SecurityUtils;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.security.UserEntity;
import com.trackme.userservice.proxy.AuthServiceFeignProxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final AuthServiceFeignProxy authServiceFeignProxy;

    public UserEntity getUser() {
        UserEntity user = null;

        log.info("retrieving user info from auth service for username [{}]", SecurityUtils.getUsername());
        CommonResponse response = authServiceFeignProxy.retrieveUser();

        if (response.isSuccess()) {
            user = (UserEntity) response.getPayload();
            log.info("user [{}] found with id [{}]", user.getUsername(), user.getId());
        } else {
            log.error("user [{}] not found", SecurityUtils.getUsername());
        }

        return user;
    }
}

package com.trackme.common.service;

import com.trackme.common.security.SecurityUtils;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.security.UserEntity;
import com.trackme.common.proxy.auth.AuthServiceFeignProxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final AuthServiceFeignProxy authServiceFeignProxy;

    public UserEntity getUser() {
        UserEntity user = null;

        log.info("retrieving user info from auth service for username [{}]", SecurityUtils.getUsername());
        ResponseEntity<CommonResponse<UserEntity>> response = authServiceFeignProxy.retrieveUser();

        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            log.error("request failed to auth service, status [{}]", response.getStatusCode());
            throw new RuntimeException("Error with retrieving user from Auth service");
        }

        if (response.getBody().isSuccess()) {
            CommonResponse body = response.getBody();
            user = (UserEntity) body.getPayload();
            log.info("user [{}] found with id [{}]", user.getUsername(), user.getId());
        } else {
            log.error("user [{}] not found", SecurityUtils.getUsername());
            log.error("user not found with response status [{}] and response error message [{}]",
                    response.getBody().getStatus(), response.getBody().getError().getErrorMessage());
            throw new UsernameNotFoundException(response.getBody().getError().getErrorMessage());
        }
        return user;
    }
}

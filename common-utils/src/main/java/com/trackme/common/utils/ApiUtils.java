package com.trackme.common.utils;

import com.trackme.common.security.SecurityUtils;
import com.trackme.common.service.UserService;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.security.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
public class ApiUtils {

    public static UserEntity getUserFromResponseEntity(ResponseEntity<CommonResponse<UserEntity>> response){
        UserEntity user;

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

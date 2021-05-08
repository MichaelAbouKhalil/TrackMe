package com.trackme.common.service;

import com.trackme.common.security.SecurityUtils;
import com.trackme.common.utils.ApiUtils;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.payload.request.retrieveuser.GetUserDetailsRequest;
import com.trackme.models.security.UserEntity;
import com.trackme.common.proxy.auth.AuthServiceFeignProxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final AuthServiceFeignProxy authServiceFeignProxy;

    public UserEntity getUser() {

        log.info("retrieving user info from auth service for username [{}]", SecurityUtils.getUsername());
        ResponseEntity<CommonResponse<UserEntity>> response = authServiceFeignProxy.retrieveUser();

        UserEntity user = ApiUtils.getUserFromResponseEntity(response);

        return user;
    }

    public UserEntity getUserDetails(GetUserDetailsRequest request) {

        log.info("location user with username/email [{}] from auth service",
                StringUtils.isEmpty(request.getUsername()) ? request.getEmail() : request.getUsername());

        ResponseEntity<CommonResponse<UserEntity>> response = authServiceFeignProxy.getUserDetails(request);

        UserEntity user = ApiUtils.getUserFromResponseEntity(response);

        return user;
    }

}

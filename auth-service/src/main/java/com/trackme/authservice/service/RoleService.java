package com.trackme.authservice.service;

import com.trackme.models.common.CommonResponse;
import com.trackme.models.payload.request.roleupdate.RoleUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    public CommonResponse promote(RoleUpdateRequest request){

        return CommonResponse.ok();
    }
}

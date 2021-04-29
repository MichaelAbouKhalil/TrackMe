package com.trackme.authservice.service;

import com.trackme.authservice.repository.RoleRepository;
import com.trackme.models.exception.RoleNotFoundException;
import com.trackme.models.security.RoleEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleEntity findRoleByRoleName(String roleName){
        RoleEntity dbRole = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> {
                    throw new RoleNotFoundException("Requested Role [" + roleName + "] not found.");
                });
        return dbRole;
    }
}

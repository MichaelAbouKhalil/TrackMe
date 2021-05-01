package com.trackme.authservice.service;

import com.trackme.authservice.Base;
import com.trackme.authservice.repository.RoleRepository;
import com.trackme.models.exception.RoleNotFoundException;
import com.trackme.models.security.RoleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class RoleServiceTest extends Base {

    @Autowired
    RoleService roleService;

    @MockBean
    RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("Find Role By Name Tests")
    @Nested
    class FindRoleByNameTests{
        @Test
        public void findRoleByName_Found() {
            RoleEntity role = RoleEntity.builder().roleName("ROLE_ADMIN").build();
            when(roleRepository.findByRoleName(any(String.class)))
                    .thenReturn(Optional.of(role));

            RoleEntity foundRole = roleService.findRoleByRoleName(role.getRoleName());

            assertEquals(role, foundRole);
        }

        @Test
        public void findRoleByName_NotFound() {
            RoleEntity role = RoleEntity.builder().roleName("ROLE_ADMIN").build();
            String message = "Requested Role [" + role.getRoleName() + "] not found.";
            when(roleRepository.findByRoleName(any(String.class)))
                    .thenThrow(new RoleNotFoundException(message));

            RoleNotFoundException ex = assertThrows(RoleNotFoundException.class,
                    () -> {
                        roleService.findRoleByRoleName(role.getRoleName());
                    });

            assertEquals(message, ex.getMessage());
        }
    }
}
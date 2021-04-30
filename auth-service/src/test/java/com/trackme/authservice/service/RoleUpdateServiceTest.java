package com.trackme.authservice.service;

import com.trackme.authservice.Base;
import com.trackme.authservice.repository.RoleRepository;
import com.trackme.authservice.repository.UserRepository;
import com.trackme.authservice.utils.OrgService;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.enums.RoleEnum;
import com.trackme.models.exception.InvalidRoleException;
import com.trackme.models.payload.request.roleupdate.RoleUpdateRequest;
import com.trackme.models.security.RoleEntity;
import com.trackme.models.security.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


class RoleUpdateServiceTest extends Base {

    @Autowired
    RoleUpdateService roleUpdateService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    RoleRepository roleRepository;

    @MockBean
    OrgService orgService;


    RoleUpdateRequest adminRoleUpdateRequest;
    RoleUpdateRequest pmRoleUpdateRequest;
    RoleUpdateRequest devRoleUpdateRequest;
    RoleUpdateRequest custRoleUpdateRequest;

    UserEntity adminUser;
    UserEntity pmUser;
    UserEntity pmPendingUser;
    UserEntity devUser;
    UserEntity devPendingUser;
    UserEntity custUser;
    UserEntity custPendingUser;

    RoleEntity adminRole;
    RoleEntity pmRole;
    RoleEntity pmPendingRole;
    RoleEntity devRole;
    RoleEntity devPendingRole;
    RoleEntity custRole;
    RoleEntity custPendingRole;

    @BeforeEach
    void setUp() {
        adminRoleUpdateRequest = RoleUpdateRequest.builder().email("admin@promote.com").build();
        pmRoleUpdateRequest = RoleUpdateRequest.builder().email("pm@promote.com").build();
        devRoleUpdateRequest = RoleUpdateRequest.builder().email("dev@promote.com").build();
        custRoleUpdateRequest = RoleUpdateRequest.builder().email("cust@promote.com").build();

        adminRole = RoleEntity.builder().roleName(RoleEnum.ADMIN.getRoleName()).build();
        pmRole = RoleEntity.builder().roleName(RoleEnum.PM.getRoleName()).build();
        pmPendingRole = RoleEntity.builder().roleName(RoleEnum.PM_PENDING.getRoleName()).build();
        devRole = RoleEntity.builder().roleName(RoleEnum.DEV.getRoleName()).build();
        devPendingRole = RoleEntity.builder().roleName(RoleEnum.DEV_PENDING.getRoleName()).build();
        custRole = RoleEntity.builder().roleName(RoleEnum.CUSTOMER.getRoleName()).build();
        custPendingRole = RoleEntity.builder().roleName(RoleEnum.CUSTOMER_PENDING.getRoleName()).build();

        adminUser = UserEntity.builder().email("admin@promote.com").role(adminRole).build();
        pmUser = UserEntity.builder().email("pm@promote.com").role(pmRole).build();
        pmPendingUser = UserEntity.builder().email("pm@promote.com").role(pmPendingRole).build();
        devUser = UserEntity.builder().email("dev@promote.com").role(devRole).build();
        devPendingUser = UserEntity.builder().email("dev@promote.com").role(devPendingRole).build();
        custUser = UserEntity.builder().email("cust@promote.com").role(custRole).build();
        custPendingUser = UserEntity.builder().email("cust@promote.com").role(custPendingRole).build();
    }

    @DisplayName("Update Admin Role")
    @Nested
    class UpdateAdminRole{
        @Test
        public void promote_ADMIN_Invalid() {
            when(userRepository.findByEmail(any(String.class)))
                    .thenReturn(Optional.of(adminUser));
            doNothing().when(orgService).validateOrg(any(UserEntity.class));
            when(roleRepository.findByRoleName(any(String.class)))
                    .thenReturn(Optional.of(adminRole));
            when(userRepository.save(any(UserEntity.class)))
                    .thenReturn(adminUser);

            InvalidRoleException invalidRoleException = assertThrows(InvalidRoleException.class,
                    () -> {
                        roleUpdateService.updateRole(adminRoleUpdateRequest, true);
                    });

            assertEquals("User has invalid Role for this request", invalidRoleException.getMessage());
        }
    }

    @DisplayName("Promote Test Cases")
    @Nested
    class PromoteTests{

        @Test
        public void promote_PM_Valid() {
            when(userRepository.findByEmail(any(String.class)))
                    .thenReturn(Optional.of(pmPendingUser));
            doNothing().when(orgService).validateOrg(any(UserEntity.class));
            when(roleRepository.findByRoleName(any(String.class)))
                    .thenReturn(Optional.of(pmRole));
            when(userRepository.save(any(UserEntity.class)))
                    .thenReturn(pmPendingUser);

            CommonResponse response = roleUpdateService.updateRole(pmRoleUpdateRequest, true);

            assertEquals(pmUser.getRoles().get(0), pmRole);
            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertTrue(response.isSuccess());
        }

        @Test
        public void promote_DEV_Valid() {
            when(userRepository.findByEmail(any(String.class)))
                    .thenReturn(Optional.of(devPendingUser));
            doNothing().when(orgService).validateOrg(any(UserEntity.class));
            when(roleRepository.findByRoleName(any(String.class)))
                    .thenReturn(Optional.of(devRole));
            when(userRepository.save(any(UserEntity.class)))
                    .thenReturn(devPendingUser);

            CommonResponse response = roleUpdateService.updateRole(devRoleUpdateRequest, true);

            assertEquals(devUser.getRoles().get(0), devRole);
            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertTrue(response.isSuccess());
        }

        @Test
        public void promote_CUSTOMER_Valid() {
            when(userRepository.findByEmail(any(String.class)))
                    .thenReturn(Optional.of(custPendingUser));
            doNothing().when(orgService).validateOrg(any(UserEntity.class));
            when(roleRepository.findByRoleName(any(String.class)))
                    .thenReturn(Optional.of(custRole));
            when(userRepository.save(any(UserEntity.class)))
                    .thenReturn(custPendingUser);

            CommonResponse response = roleUpdateService.updateRole(custRoleUpdateRequest, true);

            assertEquals(custUser.getRoles().get(0), custRole);
            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertTrue(response.isSuccess());
        }
    }

    @DisplayName("Demote test cases")
    @Nested
    class DemoteTests{
        @Test
        public void demote_PM_Valid() {
            when(userRepository.findByEmail(any(String.class)))
                    .thenReturn(Optional.of(pmUser));
            doNothing().when(orgService).validateOrg(any(UserEntity.class));
            when(roleRepository.findByRoleName(any(String.class)))
                    .thenReturn(Optional.of(pmPendingRole));
            when(userRepository.save(any(UserEntity.class)))
                    .thenReturn(pmUser);

            CommonResponse response = roleUpdateService.updateRole(pmRoleUpdateRequest, false);

            assertEquals(pmUser.getRoles().get(0), pmPendingRole);
            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertTrue(response.isSuccess());
        }
        @Test
        public void demote_DEV_Valid() {
            when(userRepository.findByEmail(any(String.class)))
                    .thenReturn(Optional.of(devUser));
            doNothing().when(orgService).validateOrg(any(UserEntity.class));
            when(roleRepository.findByRoleName(any(String.class)))
                    .thenReturn(Optional.of(devPendingRole));
            when(userRepository.save(any(UserEntity.class)))
                    .thenReturn(devUser);

            CommonResponse response = roleUpdateService.updateRole(devRoleUpdateRequest, false);

            assertEquals(devUser.getRoles().get(0), devPendingRole);
            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertTrue(response.isSuccess());
        }
        @Test
        public void demote_Customer_Valid() {
            when(userRepository.findByEmail(any(String.class)))
                    .thenReturn(Optional.of(custUser));
            doNothing().when(orgService).validateOrg(any(UserEntity.class));
            when(roleRepository.findByRoleName(any(String.class)))
                    .thenReturn(Optional.of(custPendingRole));
            when(userRepository.save(any(UserEntity.class)))
                    .thenReturn(custUser);

            CommonResponse response = roleUpdateService.updateRole(custRoleUpdateRequest, false);

            assertEquals(custUser.getRoles().get(0), custPendingRole);
            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertTrue(response.isSuccess());
        }
    }
}
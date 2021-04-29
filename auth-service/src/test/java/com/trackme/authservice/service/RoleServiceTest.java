package com.trackme.authservice.service;

import com.github.dockerjava.api.model.DeviceRequest;
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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoleServiceTest {

    @Autowired
    RoleService roleService;

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
    UserEntity devUser;
    UserEntity custUser;

    RoleEntity adminRole;
    RoleEntity pmRole;
    RoleEntity devRole;
    RoleEntity custRole;

    @BeforeEach
    void setUp() {
        adminRoleUpdateRequest = RoleUpdateRequest.builder().email("admin@promote.com").build();
        pmRoleUpdateRequest = RoleUpdateRequest.builder().email("pm@promote.com").build();
        devRoleUpdateRequest = RoleUpdateRequest.builder().email("dev@promote.com").build();
        custRoleUpdateRequest = RoleUpdateRequest.builder().email("cust@promote.com").build();

        adminRole = RoleEntity.builder().roleName(RoleEnum.ADMIN.getRoleName()).build();
        pmRole = RoleEntity.builder().roleName(RoleEnum.PM_PENDING.getRoleName()).build();
        devRole = RoleEntity.builder().roleName(RoleEnum.DEV_PENDING.getRoleName()).build();
        custRole = RoleEntity.builder().roleName(RoleEnum.CUSTOMER_PENDING.getRoleName()).build();

        adminUser = UserEntity.builder().email("admin@promote.com").role(adminRole).build();
        pmUser = UserEntity.builder().email("pm@promote.com").role(pmRole).build();
        devUser = UserEntity.builder().email("dev@promote.com").role(devRole).build();
        custUser = UserEntity.builder().email("cust@promote.com").role(custRole).build();
    }

    @Test
    public void promote_PM_Valid() {
        when(userRepository.findByEmail(any(String.class)))
                .thenReturn(Optional.of(pmUser));
        doNothing().when(orgService).validateOrg(any(UserEntity.class));
        when(roleRepository.findByRoleName(any(String.class)))
                .thenReturn(Optional.of(pmRole));
        pmUser.setRoles(Arrays.asList(pmRole));
        when(userRepository.save(any(UserEntity.class)))
                .thenReturn(pmUser);

        CommonResponse response = roleService.promote(pmRoleUpdateRequest, RoleEnum.PM);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.isSuccess());
    }

    @Test
    public void promote_DEV_Valid() {
        when(userRepository.findByEmail(any(String.class)))
                .thenReturn(Optional.of(devUser));
        doNothing().when(orgService).validateOrg(any(UserEntity.class));
        when(roleRepository.findByRoleName(any(String.class)))
                .thenReturn(Optional.of(devRole));
        devUser.setRoles(Arrays.asList(devRole));
        when(userRepository.save(any(UserEntity.class)))
                .thenReturn(devUser);

        CommonResponse response = roleService.promote(devRoleUpdateRequest, null);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.isSuccess());
    }

    @Test
    public void promote_CUSTOMER_Valid() {
        when(userRepository.findByEmail(any(String.class)))
                .thenReturn(Optional.of(custUser));
        doNothing().when(orgService).validateOrg(any(UserEntity.class));
        when(roleRepository.findByRoleName(any(String.class)))
                .thenReturn(Optional.of(custRole));
        custUser.setRoles(Arrays.asList(custRole));
        when(userRepository.save(any(UserEntity.class)))
                .thenReturn(custUser);

        CommonResponse response = roleService.promote(custRoleUpdateRequest, null);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.isSuccess());
    }

    @Test
    public void promote_ADMIN_Invalid() {
        when(userRepository.findByEmail(any(String.class)))
                .thenReturn(Optional.of(adminUser));
        doNothing().when(orgService).validateOrg(any(UserEntity.class));
        when(roleRepository.findByRoleName(any(String.class)))
                .thenReturn(Optional.of(adminRole));
        adminUser.setRoles(Arrays.asList(adminRole));
        when(userRepository.save(any(UserEntity.class)))
                .thenReturn(adminUser);

        InvalidRoleException invalidRoleException = assertThrows(InvalidRoleException.class,
                () -> {
                    roleService.promote(adminRoleUpdateRequest, RoleEnum.ADMIN);
                });

        assertEquals("User has invalid Role for this request", invalidRoleException.getMessage());
    }

    @Test
    public void promote_DEV_DEV_Invalid() {
        when(userRepository.findByEmail(any(String.class)))
                .thenReturn(Optional.of(devUser));
        doNothing().when(orgService).validateOrg(any(UserEntity.class));
        when(roleRepository.findByRoleName(any(String.class)))
                .thenReturn(Optional.of(devRole));
        devUser.setRoles(Arrays.asList(devRole));
        when(userRepository.save(any(UserEntity.class)))
                .thenReturn(devUser);

        InvalidRoleException invalidRoleException = assertThrows(InvalidRoleException.class,
                () -> {
                    roleService.promote(devRoleUpdateRequest, RoleEnum.DEV_PENDING);
                });

        assertEquals("User has invalid Role for this request", invalidRoleException.getMessage());
    }

    @Test
    public void promote_DEV_CUSTOMER_Invalid() {
        when(userRepository.findByEmail(any(String.class)))
                .thenReturn(Optional.of(devUser));
        doNothing().when(orgService).validateOrg(any(UserEntity.class));
        when(roleRepository.findByRoleName(any(String.class)))
                .thenReturn(Optional.of(custRole));
        devUser.setRoles(Arrays.asList(custRole));
        when(userRepository.save(any(UserEntity.class)))
                .thenReturn(devUser);

        InvalidRoleException invalidRoleException = assertThrows(InvalidRoleException.class,
                () -> {
                    roleService.promote(devRoleUpdateRequest, RoleEnum.CUSTOMER_PENDING);
                });

        assertEquals("User has invalid Role for this request", invalidRoleException.getMessage());
    }

    @Test
    public void promote_CUSTOMER_DEV_Invalid() {
        when(userRepository.findByEmail(any(String.class)))
                .thenReturn(Optional.of(custUser));
        doNothing().when(orgService).validateOrg(any(UserEntity.class));
        when(roleRepository.findByRoleName(any(String.class)))
                .thenReturn(Optional.of(devRole));
        custUser.setRoles(Arrays.asList(devRole));
        when(userRepository.save(any(UserEntity.class)))
                .thenReturn(custUser);

        InvalidRoleException invalidRoleException = assertThrows(InvalidRoleException.class,
                () -> {
                    roleService.promote(custRoleUpdateRequest, RoleEnum.DEV_PENDING);
                });

        assertEquals("User has invalid Role for this request", invalidRoleException.getMessage());
    }

    @Test
    public void promote_CUSTOMER_CUSTOMER_Invalid() {
        when(userRepository.findByEmail(any(String.class)))
                .thenReturn(Optional.of(custUser));
        doNothing().when(orgService).validateOrg(any(UserEntity.class));
        when(roleRepository.findByRoleName(any(String.class)))
                .thenReturn(Optional.of(custRole));
        custUser.setRoles(Arrays.asList(custRole));
        when(userRepository.save(any(UserEntity.class)))
                .thenReturn(custUser);

        InvalidRoleException invalidRoleException = assertThrows(InvalidRoleException.class,
                () -> {
                    roleService.promote(custRoleUpdateRequest, RoleEnum.CUSTOMER);
                });

        assertEquals("User has invalid Role for this request", invalidRoleException.getMessage());
    }
}
package com.trackme.authservice.utils;

import com.trackme.authservice.Base;
import com.trackme.authservice.repository.RoleRepository;
import com.trackme.authservice.repository.UserRepository;
import com.trackme.models.enums.RoleEnum;
import com.trackme.models.exception.InvalidOperationException;
import com.trackme.models.security.RoleEntity;
import com.trackme.models.security.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class OrgServiceTest extends Base {

    @Autowired
    OrgService orgService;

    @Autowired
    RoleRepository roleRepository;

    @MockBean
    UserRepository userRepository;

    @Test
    @WithMockUser(username = "pm", roles = {"PM"})
    public void validateOrg_PmDev_SameOrg_Valid() {
        RoleEntity pmRole = roleRepository.findByRoleName(RoleEnum.PM.getRoleName()).orElseThrow();
        RoleEntity devPendingRole = roleRepository.findByRoleName(RoleEnum.DEV_PENDING.getRoleName()).orElseThrow();
        UserEntity pmUser = UserEntity.builder().username("pm").orgId("123").role(pmRole).build();
        UserEntity devPendingUser = UserEntity.builder().username("dev_pending").orgId("123").role(devPendingRole).build();

        when(userRepository.findByUsername(any(String.class)))
                .thenReturn(Optional.ofNullable(pmUser));

        orgService.validateOrg(devPendingUser);

        // if nothing happens means it's successful
    }

    @Test
    @WithMockUser(username = "pm", roles = {"PM"})
    public void validateOrg_PmCustomer_SameOrg_Valid() {
        RoleEntity pmRole = roleRepository.findByRoleName(RoleEnum.PM.getRoleName()).orElseThrow();
        RoleEntity custPendingRole = roleRepository.findByRoleName(RoleEnum.CUSTOMER_PENDING.getRoleName()).orElseThrow();
        UserEntity pmUser = UserEntity.builder().username("pm").orgId("123").role(pmRole).build();
        UserEntity custPendingUser = UserEntity.builder().username("cust_pending").orgId("123").role(custPendingRole).build();

        when(userRepository.findByUsername(any(String.class)))
                .thenReturn(Optional.ofNullable(pmUser));

        orgService.validateOrg(custPendingUser);

        // if nothing happens means it's successful
    }

    @Test
    @WithMockUser(username = "pm", roles = {"PM"})
    public void validateOrg_PmDev_SameOrg_Invalid() {
        RoleEntity pmRole = roleRepository.findByRoleName(RoleEnum.PM.getRoleName()).orElseThrow();
        RoleEntity devPendingRole = roleRepository.findByRoleName(RoleEnum.DEV_PENDING.getRoleName()).orElseThrow();
        UserEntity pmUser = UserEntity.builder().username("pm").orgId("1234").role(pmRole).build();
        UserEntity devPendingUser = UserEntity.builder().username("dev_pending").orgId("123").role(devPendingRole).build();

        when(userRepository.findByUsername(any(String.class)))
                .thenReturn(Optional.ofNullable(pmUser));

        InvalidOperationException exception = assertThrows(InvalidOperationException.class,
                () -> orgService.validateOrg(devPendingUser));

        assertEquals("Different org Id", exception.getMessage());
    }

    @Test
    @WithMockUser(username = "pm", roles = {"PM"})
    public void validateOrg_PmCustomer_SameOrg_Invalid() {
        RoleEntity pmRole = roleRepository.findByRoleName(RoleEnum.PM.getRoleName()).orElseThrow();
        RoleEntity custPendingRole = roleRepository.findByRoleName(RoleEnum.CUSTOMER_PENDING.getRoleName()).orElseThrow();
        UserEntity pmUser = UserEntity.builder().username("pm").orgId("1234").role(pmRole).build();
        UserEntity custPendingUser = UserEntity.builder().username("cust_pending").orgId("123").role(custPendingRole).build();

        when(userRepository.findByUsername(any(String.class)))
                .thenReturn(Optional.ofNullable(pmUser));

        InvalidOperationException exception = assertThrows(InvalidOperationException.class,
                () -> orgService.validateOrg(custPendingUser));

        assertEquals("Different org Id", exception.getMessage());
    }

    @Test
    @WithMockUser(username = "dev", roles = {"DEV"})
    public void validateOrg_DevCustomer_Invalid() {
        RoleEntity devRole = roleRepository.findByRoleName(RoleEnum.DEV.getRoleName()).orElseThrow();
        RoleEntity custPendingRole = roleRepository.findByRoleName(RoleEnum.CUSTOMER_PENDING.getRoleName()).orElseThrow();
        UserEntity devUser = UserEntity.builder().username("dev").orgId("1234").role(devRole).build();
        UserEntity custPendingUser = UserEntity.builder().username("cust_pending").orgId("123").role(custPendingRole).build();

        when(userRepository.findByUsername(any(String.class)))
                .thenReturn(Optional.ofNullable(devUser));

        InvalidOperationException exception = assertThrows(InvalidOperationException.class,
                () -> orgService.validateOrg(custPendingUser));

        assertEquals("user [" + devUser.getUsername() + "] " +
                "doesn't have authority to perform this action", exception.getMessage());
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    public void validateOrg_CustomerDev_Invalid() {
        RoleEntity custRole = roleRepository.findByRoleName(RoleEnum.CUSTOMER.getRoleName()).orElseThrow();
        RoleEntity devPendingRole = roleRepository.findByRoleName(RoleEnum.DEV.getRoleName()).orElseThrow();
        UserEntity custUser = UserEntity.builder().username("cust").orgId("1234").role(custRole).build();
        UserEntity devPendingUser = UserEntity.builder().username("dev_pending").orgId("123").role(devPendingRole).build();

        when(userRepository.findByUsername(any(String.class)))
                .thenReturn(Optional.ofNullable(custUser));

        InvalidOperationException exception = assertThrows(InvalidOperationException.class,
                () -> orgService.validateOrg(devPendingUser));

        assertEquals("user [" + custUser.getUsername() + "] " +
                "doesn't have authority to perform this action", exception.getMessage());
    }

    @Test
    @WithMockUser(username = "dev", roles = {"DEV"})
    public void validateOrg_DevDev_Invalid() {
        RoleEntity devRole = roleRepository.findByRoleName(RoleEnum.DEV.getRoleName()).orElseThrow();
        RoleEntity devPendingRole = roleRepository.findByRoleName(RoleEnum.DEV.getRoleName()).orElseThrow();
        UserEntity devUser = UserEntity.builder().username("dev").orgId("1234").role(devRole).build();
        UserEntity devPendingUser = UserEntity.builder().username("dev_pending").orgId("123").role(devPendingRole).build();

        when(userRepository.findByUsername(any(String.class)))
                .thenReturn(Optional.ofNullable(devUser));

        InvalidOperationException exception = assertThrows(InvalidOperationException.class,
                () -> orgService.validateOrg(devPendingUser));

        assertEquals("user [" + devUser.getUsername() + "] " +
                "doesn't have authority to perform this action", exception.getMessage());
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    public void validateOrg_CustomerCustomer_Invalid() {
        RoleEntity custRole = roleRepository.findByRoleName(RoleEnum.CUSTOMER.getRoleName()).orElseThrow();
        RoleEntity custPendingRole = roleRepository.findByRoleName(RoleEnum.CUSTOMER_PENDING.getRoleName()).orElseThrow();
        UserEntity custUser = UserEntity.builder().username("cust").orgId("1234").role(custRole).build();
        UserEntity custPendingUser = UserEntity.builder().username("cust_pending").orgId("123").role(custPendingRole).build();

        when(userRepository.findByUsername(any(String.class)))
                .thenReturn(Optional.ofNullable(custUser));

        InvalidOperationException exception = assertThrows(InvalidOperationException.class,
                () -> orgService.validateOrg(custPendingUser));

        assertEquals("user [" + custUser.getUsername() + "] " +
                "doesn't have authority to perform this action", exception.getMessage());
    }
}
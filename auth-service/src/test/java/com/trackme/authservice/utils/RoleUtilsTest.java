package com.trackme.authservice.utils;

import com.trackme.authservice.repository.RoleRepository;
import com.trackme.models.enums.RoleEnum;
import com.trackme.models.security.RoleEntity;
import org.checkerframework.checker.units.qual.A;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RoleUtilsTest {

    RoleEntity adminRole;
    RoleEntity pmPendingRole;
    RoleEntity devPendingRole;
    RoleEntity customerPendingRole;

    @BeforeEach
    void setUp() {
        adminRole = RoleEntity.builder().roleName(RoleEnum.ADMIN.getRoleName()).build();
        pmPendingRole = RoleEntity.builder().roleName(RoleEnum.PM_PENDING.getRoleName()).build();
        devPendingRole = RoleEntity.builder().roleName(RoleEnum.DEV_PENDING.getRoleName()).build();
        customerPendingRole = RoleEntity.builder().roleName(RoleEnum.CUSTOMER_PENDING.getRoleName()).build();
    }

    @Test
    public void getPromoteRole_PM_Valid() {
        RoleEnum promoteRole = RoleUtils.getPromoteRole(pmPendingRole, RoleEnum.PM);
        assertEquals(RoleEnum.PM, promoteRole);
    }

    @Test
    public void getPromoteRole_DEV_Valid() {
        RoleEnum promoteRole = RoleUtils.getPromoteRole(devPendingRole, null);
        assertEquals(RoleEnum.DEV, promoteRole);
    }

    @Test
    public void getPromoteRole_CUSTOMER_Valid() {
        RoleEnum promoteRole = RoleUtils.getPromoteRole(customerPendingRole, null);
        assertEquals(RoleEnum.CUSTOMER, promoteRole);
    }

    @Test
    public void getPromoteRole_ADMIN_Invalid() {
        RoleEnum promoteRole = RoleUtils.getPromoteRole(adminRole, RoleEnum.ADMIN);
        assertNull(promoteRole);
    }

    @Test
    public void getPromoteRole_PM_Invalid() {
        RoleEnum promoteRole = RoleUtils.getPromoteRole(pmPendingRole, null);
        assertNull(promoteRole);
    }

    @Test
    public void getPromoteRole_DEV_Invalid() {
        RoleEnum promoteRole = RoleUtils.getPromoteRole(devPendingRole, RoleEnum.PM);
        assertNull(promoteRole);
    }

    @Test
    public void getPromoteRole_Customer_Invalid() {
        RoleEnum promoteRole = RoleUtils.getPromoteRole(customerPendingRole, RoleEnum.DEV);
        assertNull(promoteRole);
    }
}
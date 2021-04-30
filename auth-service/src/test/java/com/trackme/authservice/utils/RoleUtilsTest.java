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
    RoleEntity pmRole;
    RoleEntity pmPendingRole;
    RoleEntity devRole;
    RoleEntity devPendingRole;
    RoleEntity customerRole;
    RoleEntity customerPendingRole;

    @BeforeEach
    void setUp() {
        adminRole = RoleEntity.builder().roleName(RoleEnum.ADMIN.getRoleName()).build();
        pmRole = RoleEntity.builder().roleName(RoleEnum.PM.getRoleName()).build();
        pmPendingRole = RoleEntity.builder().roleName(RoleEnum.PM_PENDING.getRoleName()).build();
        devRole = RoleEntity.builder().roleName(RoleEnum.DEV.getRoleName()).build();
        devPendingRole = RoleEntity.builder().roleName(RoleEnum.DEV_PENDING.getRoleName()).build();
        customerRole = RoleEntity.builder().roleName(RoleEnum.CUSTOMER.getRoleName()).build();
        customerPendingRole = RoleEntity.builder().roleName(RoleEnum.CUSTOMER_PENDING.getRoleName()).build();
    }

    @Test
    public void getPromoteRole_PM_Valid() {
        RoleEnum promoteRole = RoleUtils.getUpdateRole(pmPendingRole, true);
        assertEquals(RoleEnum.PM, promoteRole);
    }

    @Test
    public void getPromoteRole_DEV_Valid() {
        RoleEnum promoteRole = RoleUtils.getUpdateRole(devPendingRole, true);
        assertEquals(RoleEnum.DEV, promoteRole);
    }

    @Test
    public void getPromoteRole_CUSTOMER_Valid() {
        RoleEnum promoteRole = RoleUtils.getUpdateRole(customerPendingRole, true);
        assertEquals(RoleEnum.CUSTOMER, promoteRole);
    }

    @Test
    public void getPromoteRole_ADMIN_Invalid() {
        RoleEnum promoteRole = RoleUtils.getUpdateRole(adminRole, true);
        assertNull(promoteRole);
    }

    @Test
    public void getDemoteRole_PM_Valid() {
        RoleEnum promoteRole = RoleUtils.getUpdateRole(pmRole, false);
        assertEquals(RoleEnum.PM_PENDING, promoteRole);
    }

    @Test
    public void getDemoteRole_DEV_VALID() {
        RoleEnum promoteRole = RoleUtils.getUpdateRole(devRole, false);
        assertEquals(RoleEnum.DEV_PENDING, promoteRole);
    }

    @Test
    public void getDemoteRole_Customer_Valid() {
        RoleEnum promoteRole = RoleUtils.getUpdateRole(customerRole, false);
        assertEquals(RoleEnum.CUSTOMER_PENDING, promoteRole);
    }
}
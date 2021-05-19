package com.trackme.authservice.utils;

import com.trackme.authservice.Base;
import com.trackme.authservice.repository.RoleRepository;
import com.trackme.authservice.repository.UserRepository;
import com.trackme.authservice.service.AuthUserService;
import com.trackme.common.security.SecurityUtils;
import com.trackme.common.service.UserService;
import com.trackme.models.enums.RoleEnum;
import com.trackme.models.exception.InvalidOperationException;
import com.trackme.models.security.RoleEntity;
import com.trackme.models.security.UserEntity;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrgServiceTest extends Base {

    @Autowired
    OrgService orgService;

    @Autowired
    RoleRepository roleRepository;

    @MockBean
    UserRepository userRepository;

    @DisplayName("Validate Org Tests")
    @Nested
    class ValidateOrgTests {
        MockedStatic<SecurityUtils> mockedStatic;
        @BeforeEach
        void setUp() {
            mockedStatic = mockStatic(SecurityUtils.class);
        }

        @AfterEach
        void tearDown() {
            mockedStatic.close();
        }

        @Test
        public void validateOrgPromoteDemote_PmDev_SameOrg_Valid() {
            RoleEntity pmRole = roleRepository.findByRoleName(RoleEnum.PM.getRoleName()).orElseThrow();
            RoleEntity devPendingRole = roleRepository.findByRoleName(RoleEnum.DEV_PENDING.getRoleName()).orElseThrow();
            UserEntity pmUser = UserEntity.builder().username("pm").orgId("123").role(pmRole).build();
            UserEntity devPendingUser = UserEntity.builder().username("dev_pending").orgId("123").role(devPendingRole).build();

            when(userRepository.findByUsername(any(String.class)))
                    .thenReturn(Optional.ofNullable(pmUser));
            mockedStatic.when(SecurityUtils::getUsername).thenReturn("pm");

            orgService.validateOrgPromoteDemote(devPendingUser);

            // if nothing happens means it's successful
        }

        @Test
        public void validateOrgPromoteDemote_PmCustomer_SameOrg_Valid() {
            RoleEntity pmRole = roleRepository.findByRoleName(RoleEnum.PM.getRoleName()).orElseThrow();
            RoleEntity custPendingRole = roleRepository.findByRoleName(RoleEnum.CUSTOMER_PENDING.getRoleName()).orElseThrow();
            UserEntity pmUser = UserEntity.builder().username("pm").orgId("123").role(pmRole).build();
            UserEntity custPendingUser = UserEntity.builder().username("cust_pending").orgId("123").role(custPendingRole).build();

            when(userRepository.findByUsername(any(String.class)))
                    .thenReturn(Optional.ofNullable(pmUser));
            mockedStatic.when(SecurityUtils::getUsername).thenReturn("pm");

            orgService.validateOrgPromoteDemote(custPendingUser);

            // if nothing happens means it's successful
        }

        @Test
        public void validateOrgPromoteDemote_PmDev_SameOrg_Invalid() {
            RoleEntity pmRole = roleRepository.findByRoleName(RoleEnum.PM.getRoleName()).orElseThrow();
            RoleEntity devPendingRole = roleRepository.findByRoleName(RoleEnum.DEV_PENDING.getRoleName()).orElseThrow();
            UserEntity pmUser = UserEntity.builder().username("pm").orgId("1234").role(pmRole).build();
            UserEntity devPendingUser = UserEntity.builder().username("dev_pending").orgId("123").role(devPendingRole).build();

            when(userRepository.findByUsername(any(String.class)))
                    .thenReturn(Optional.ofNullable(pmUser));
            mockedStatic.when(SecurityUtils::getUsername).thenReturn("pm");

            InvalidOperationException exception = assertThrows(InvalidOperationException.class,
                    () -> orgService.validateOrgPromoteDemote(devPendingUser));

            assertEquals("Different org Id", exception.getMessage());
        }

        @Test
        public void validateOrgPromoteDemote_PmCustomer_SameOrg_Invalid() {
            RoleEntity pmRole = roleRepository.findByRoleName(RoleEnum.PM.getRoleName()).orElseThrow();
            RoleEntity custPendingRole = roleRepository.findByRoleName(RoleEnum.CUSTOMER_PENDING.getRoleName()).orElseThrow();
            UserEntity pmUser = UserEntity.builder().username("pm").orgId("1234").role(pmRole).build();
            UserEntity custPendingUser = UserEntity.builder().username("cust_pending").orgId("123").role(custPendingRole).build();

            when(userRepository.findByUsername(any(String.class)))
                    .thenReturn(Optional.ofNullable(pmUser));
            mockedStatic.when(SecurityUtils::getUsername).thenReturn("pm");

            InvalidOperationException exception = assertThrows(InvalidOperationException.class,
                    () -> orgService.validateOrgPromoteDemote(custPendingUser));

            assertEquals("Different org Id", exception.getMessage());
        }

        @Test
        public void validateOrgPromoteDemote_DevCustomer_Invalid() {
            RoleEntity devRole = roleRepository.findByRoleName(RoleEnum.DEV.getRoleName()).orElseThrow();
            RoleEntity custPendingRole = roleRepository.findByRoleName(RoleEnum.CUSTOMER_PENDING.getRoleName()).orElseThrow();
            UserEntity devUser = UserEntity.builder().username("dev").orgId("1234").role(devRole).build();
            UserEntity custPendingUser = UserEntity.builder().username("cust_pending").orgId("123").role(custPendingRole).build();

            when(userRepository.findByUsername(any(String.class)))
                    .thenReturn(Optional.ofNullable(devUser));
            mockedStatic.when(SecurityUtils::getUsername).thenReturn("dev");

            InvalidOperationException exception = assertThrows(InvalidOperationException.class,
                    () -> orgService.validateOrgPromoteDemote(custPendingUser));

            assertEquals("user [" + devUser.getUsername() + "] " +
                    "doesn't have authority to perform this action", exception.getMessage());
        }

        @Test
        public void validateOrgPromoteDemote_CustomerDev_Invalid() {
            RoleEntity custRole = roleRepository.findByRoleName(RoleEnum.CUSTOMER.getRoleName()).orElseThrow();
            RoleEntity devPendingRole = roleRepository.findByRoleName(RoleEnum.DEV.getRoleName()).orElseThrow();
            UserEntity custUser = UserEntity.builder().username("cust").orgId("1234").role(custRole).build();
            UserEntity devPendingUser = UserEntity.builder().username("dev_pending").orgId("123").role(devPendingRole).build();

            when(userRepository.findByUsername(any(String.class)))
                    .thenReturn(Optional.ofNullable(custUser));
            mockedStatic.when(SecurityUtils::getUsername).thenReturn("customer");

            InvalidOperationException exception = assertThrows(InvalidOperationException.class,
                    () -> orgService.validateOrgPromoteDemote(devPendingUser));

            assertEquals("user [" + custUser.getUsername() + "] " +
                    "doesn't have authority to perform this action", exception.getMessage());
        }

        @Test
        public void validateOrgPromoteDemote_DevDev_Invalid() {
            RoleEntity devRole = roleRepository.findByRoleName(RoleEnum.DEV.getRoleName()).orElseThrow();
            RoleEntity devPendingRole = roleRepository.findByRoleName(RoleEnum.DEV.getRoleName()).orElseThrow();
            UserEntity devUser = UserEntity.builder().username("dev").orgId("1234").role(devRole).build();
            UserEntity devPendingUser = UserEntity.builder().username("dev_pending").orgId("123").role(devPendingRole).build();

            when(userRepository.findByUsername(any(String.class)))
                    .thenReturn(Optional.ofNullable(devUser));
            mockedStatic.when(SecurityUtils::getUsername).thenReturn("dev");

            InvalidOperationException exception = assertThrows(InvalidOperationException.class,
                    () -> orgService.validateOrgPromoteDemote(devPendingUser));

            assertEquals("user [" + devUser.getUsername() + "] " +
                    "doesn't have authority to perform this action", exception.getMessage());
        }

        @Test
        public void validateOrgPromoteDemote_CustomerCustomer_Invalid() {
            RoleEntity custRole = roleRepository.findByRoleName(RoleEnum.CUSTOMER.getRoleName()).orElseThrow();
            RoleEntity custPendingRole = roleRepository.findByRoleName(RoleEnum.CUSTOMER_PENDING.getRoleName()).orElseThrow();
            UserEntity custUser = UserEntity.builder().username("cust").orgId("1234").role(custRole).build();
            UserEntity custPendingUser = UserEntity.builder().username("cust_pending").orgId("123").role(custPendingRole).build();

            when(userRepository.findByUsername(any(String.class)))
                    .thenReturn(Optional.ofNullable(custUser));
            mockedStatic.when(SecurityUtils::getUsername).thenReturn("customer");

            InvalidOperationException exception = assertThrows(InvalidOperationException.class,
                    () -> orgService.validateOrgPromoteDemote(custPendingUser));

            assertEquals("user [" + custUser.getUsername() + "] " +
                    "doesn't have authority to perform this action", exception.getMessage());
        }
    }

    @DisplayName("Validate Same Org Tests")
    @Nested
    class ValidateSameOrgTests {
        String orgId;

        UserEntity admin;
        UserEntity pm;
        UserEntity dev;
        UserEntity cust;

        AuthUserService spy;

        @BeforeEach
        void setUp() {
            orgId = "test-org";

            admin = UserEntity.builder().username("test-admin").email("admin@test.com")
                    .role(RoleEntity.builder().roleName(RoleEnum.ADMIN.getRoleName())
                            .build()).build();

            pm = UserEntity.builder().username("test-pm").email("pm@test.com")
                    .role(RoleEntity.builder().roleName(RoleEnum.PM.getRoleName())
                            .build()).orgId(orgId).build();

            dev = UserEntity.builder().username("test-dev").email("dev@test.com")
                    .role(RoleEntity.builder().roleName(RoleEnum.DEV.getRoleName())
                            .build()).orgId(orgId).build();

            cust = UserEntity.builder().username("test-cust").email("cust@test.com")
                    .role(RoleEntity.builder().roleName(RoleEnum.CUSTOMER.getRoleName())
                            .build()).orgId(orgId).build();

            spy = spy(new AuthUserService(userRepository, orgService));
        }

        @Test
        public void validateSameOrg_Admin_Admin_Valid() {
            doReturn(admin).when(spy).findAuthUser();

            UserEntity user = admin;
            user.setUsername("admin");
            assertDoesNotThrow(() -> orgService.checkSameOrg(user, admin));
        }

        @Test
        public void validateSameOrg_Admin_PM_Valid() {
            doReturn(admin).when(spy).findAuthUser();

            assertDoesNotThrow(() -> orgService.checkSameOrg(pm, admin));
        }

        @Test
        public void validateSameOrg_PM_Admin_Valid() {
            doReturn(pm).when(spy).findAuthUser();

            assertDoesNotThrow(() -> orgService.checkSameOrg(admin, pm));
        }

        @Test
        public void validateSameOrg_PM_Cust_SameOrg_Valid() {
            doReturn(pm).when(spy).findAuthUser();

            UserEntity userEntity = cust;
            assertDoesNotThrow(() -> orgService.checkSameOrg(userEntity, pm));
        }

        @Test
        public void validateSameOrg_PM_DEV_DiffOrg_Invalid() {
            doReturn(pm).when(spy).findAuthUser();

            UserEntity userEntity = dev;
            dev.setOrgId("tesstt");
            assertThrows(
                    InvalidOperationException.class,
                    () -> {
                        orgService.checkSameOrg(userEntity, pm);
                    }
            );
        }

        @Test
        public void validateSameOrg_Dev_PM_SameOrg_Valid() {
            doReturn(dev).when(spy).findAuthUser();

            assertDoesNotThrow(() -> orgService.checkSameOrg(pm, dev));
        }

        @Test
        public void validateSameOrg_Dev_Cust_DiffOrg_Invalid() {
            doReturn(dev).when(spy).findAuthUser();

            UserEntity userEntity = cust;
            userEntity.setOrgId("tesstt");
            assertThrows(
                    InvalidOperationException.class,
                    () -> {
                        orgService.checkSameOrg(userEntity, dev);
                    }
            );
        }

        @Test
        public void validateSameOrg_Cust_PM_SameOrg_Valid() {
            doReturn(cust).when(spy).findAuthUser();

            assertDoesNotThrow(() -> orgService.checkSameOrg(pm, cust));
        }

        @Test
        public void validateSameOrg_Cust_Dev_DiffOrg_Invalid() {
            doReturn(cust).when(spy).findAuthUser();

            UserEntity userEntity = dev;
            userEntity.setUsername("dev");
            userEntity.setOrgId("tesstt");
            assertThrows(
                    InvalidOperationException.class,
                    () -> {
                        orgService.checkSameOrg(userEntity, cust);
                    }
            );
        }
    }

}
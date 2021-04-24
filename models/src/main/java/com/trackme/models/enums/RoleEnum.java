package com.trackme.models.enums;

public enum RoleEnum {

    ADMIN("ROLE_ADMIN"),
    PM("ROLE_PM"),
    PM_PENDING("ROLE_PM_PENDING"),
    DEV("ROLE_DEV"),
    DEV_PENDING("ROLE_DEV_PENDING"),
    CUSTOMER("ROLE_CUSTOMER"),
    CUSTOMER_PENDING("ROLE_CUSTOMER_PENDING");

    private String roleName;

    RoleEnum(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}

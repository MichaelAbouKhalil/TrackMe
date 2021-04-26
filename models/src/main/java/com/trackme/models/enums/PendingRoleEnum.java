package com.trackme.models.enums;

public enum PendingRoleEnum {

    PM_PENDING("ROLE_PM_PENDING"),
    DEV_PENDING("ROLE_DEV_PENDING"),
    CUSTOMER_PENDING("ROLE_CUSTOMER_PENDING");

    private String roleName;

    PendingRoleEnum(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}

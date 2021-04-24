package com.trackme.models.enums;

public enum AuthorityEnum {

    ADMIN_READ("admin.read"),
    ADMIN_EDIT("admin.edit"),
    ADMIN_UPDATE("admin.update"),
    ADMIN_DELETE("admin.delete"),
    ADMIN_PROMOTE("admin.promote"),
    ADMIN_DEMOTE("admin.demote"),
    PM_READ("admin.read"),
    PM_WRITE("admin.write"),
    PM_UPDATE("admin.update"),
    PM_DELETE("admin.delete"),
    PM_ASSIGN("admin.assign"),
    PM_DISMISS("admin.dismiss"),
    DEV_READ("dev.read"),
    DEV_WRITE("dev.write"),
    DEV_UPDATE("dev.update"),
    DEV_DELETE("dev.delete"),
    CUSTOMER_READ("customer.read"),
    CUSTOMER_WRITE("customer.write"),
    CUSTOMER_UPDATE("customer.update"),
    CUSTOMER_DELETE("customer.delete");

    private String authorityName;

    AuthorityEnum(String authorityName) {
        this.authorityName = authorityName;
    }

    public String getAuthorityName() {
        return authorityName;
    }
}

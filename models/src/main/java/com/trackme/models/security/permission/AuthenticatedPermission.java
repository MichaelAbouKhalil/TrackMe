package com.trackme.models.security.permission;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * this permission is to get user info
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyRole({'ROLE_ADMIN', 'ROLE_PM', 'ROLE_DEV', 'ROLE_CUSTOMER'})")
public @interface AuthenticatedPermission {
}

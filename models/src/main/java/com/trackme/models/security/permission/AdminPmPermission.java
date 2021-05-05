package com.trackme.models.security.permission;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * this permission is for ADMIN or PM Role
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PM')")
public @interface AdminPmPermission {
}

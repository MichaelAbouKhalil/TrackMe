package com.trackme.models.security.permission;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * this permission is to enable ADMIN or PM
 * to promote DEV(or CUSTOMER)_PENDING to DEV(or CUSTOMER)
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PM')")
public @interface PromotePermission {
}

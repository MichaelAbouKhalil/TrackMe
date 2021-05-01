package com.trackme.models.security.permission;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * this permission is to get user info
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("isFullyAuthenticated()")
public @interface GetUserPermission {
}

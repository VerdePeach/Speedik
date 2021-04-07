package com.in726.app.enums;

import io.javalin.core.security.Role;

/**
 * Enumeration of roles for users.
 */
public enum Roles implements Role {
    ANYONE,
    USER,
    USER_VIP,
    ADMIN
}
package com.invault.inventory.roles;

/**
 * Defines the official role names used by InVault.
 *
 * Using an enum prevents invalid role names from being stored in the database.
 */
public enum RoleName {

    ADMIN,
    SUPERVISOR,
    WAREHOUSE,
    READ_ONLY
}

/*
 * RoleName centralizes the valid system roles.
 * This avoids using random text values like "admin", "user" or "manager"
 * and keeps permissions consistent across the application.
 */
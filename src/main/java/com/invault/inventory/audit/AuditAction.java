package com.invault.inventory.audit;

/**
 * Defines the main auditable actions inside InVault.
 *
 * Audit actions help classify what happened in the system.
 */
public enum AuditAction {

    CREATED,
    UPDATED,
    DEACTIVATED,
    DELETED,
    STOCK_MOVEMENT_CREATED,
    LOGIN_SUCCESS,
    LOGIN_FAILED,
    PASSWORD_CHANGED
}

/*
 * AuditAction centralizes the main actions that can be recorded in the audit log.
 * Using an enum avoids random text values and keeps audit records consistent.
 */
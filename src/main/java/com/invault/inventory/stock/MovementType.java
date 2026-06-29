package com.invault.inventory.stock;

/**
 * Defines the valid stock movement types in InVault.
 *
 * The movement type explains why and how stock changed.
 */
public enum MovementType {

    INBOUND,
    OUTBOUND,
    POSITIVE_ADJUSTMENT,
    NEGATIVE_ADJUSTMENT
}

/*
 * MovementType centralizes the valid types of stock movements.
 * INBOUND increases stock, OUTBOUND decreases stock, and adjustments are used
 * to correct inventory differences detected during reviews or counts.
 */
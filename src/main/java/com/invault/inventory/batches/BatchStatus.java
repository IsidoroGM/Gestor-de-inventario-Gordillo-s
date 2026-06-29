package com.invault.inventory.batches;

/**
 * Defines the possible states of an inventory batch.
 *
 * Batch status helps determine if a batch can be used for stock calculations
 * and outbound movements.
 */
public enum BatchStatus {

    AVAILABLE,
    BLOCKED,
    CONSUMED,
    INACTIVE
}

/*
 * BatchStatus centralizes the valid states for product batches.
 * AVAILABLE batches can be used normally, BLOCKED batches are temporarily unavailable,
 * CONSUMED batches have no remaining stock, and INACTIVE batches are disabled.
 */
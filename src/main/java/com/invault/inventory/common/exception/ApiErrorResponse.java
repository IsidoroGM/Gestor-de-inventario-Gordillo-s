package com.invault.inventory.common.exception;

import java.time.LocalDateTime;

/**
 * Standard error response returned by the API when something goes wrong.
 * Using a record keeps the object immutable and simple.
 */
public record ApiErrorResponse(
        int status,
        String error,
        String message,
        String path,
        LocalDateTime timestamp
) {
}

/*
 * This class defines the common JSON structure for API errors.
 * It helps the frontend receive predictable error responses from the backend.
 */
package com.invault.inventory.common.exception;

/**
 * Exception used when the client sends invalid data or breaks a business rule.
 * Example: trying to remove more stock than available.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}

/*
 * This class represents a 400 Bad Request error in the application.
 * We will use it when the request is technically valid but not acceptable for our business rules.
 */
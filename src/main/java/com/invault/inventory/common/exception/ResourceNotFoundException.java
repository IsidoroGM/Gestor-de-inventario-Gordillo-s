package com.invault.inventory.common.exception;

/**
 * Exception used when a requested resource does not exist.
 * Example: product, user, batch, category or supplier not found.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}

/*
 * This class represents a 404 Not Found error in the application.
 * We will use it when an entity cannot be found in the database.
 */
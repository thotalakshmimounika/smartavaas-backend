package com.smartavaas.exception;

public class MaintenanceNotFoundException extends RuntimeException {
    public MaintenanceNotFoundException(String message) {
        super(message);
    }

    public MaintenanceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

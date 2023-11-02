package com.soa.canete.transaccional_allocation_soa_canete.exception;

public class ResourceNotFoundException extends RuntimeException {
    private static final String DESCRIPTION = "Resource Not Found Exception";

    public ResourceNotFoundException(String detail) {
        super(String.format("%s - %s", DESCRIPTION, detail));
    }
}
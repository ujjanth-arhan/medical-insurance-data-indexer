package com.example.medicalinsurancedataindexer.plans;

public class PlanAlreadyExistsException extends RuntimeException {
    public PlanAlreadyExistsException(String message) {
        super(message);
    }
}

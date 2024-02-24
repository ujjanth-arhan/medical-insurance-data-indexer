package com.example.medicalinsurancedataindexer.plans;

public class PlanServiceException extends RuntimeException {
    public PlanServiceException(String errorMessage) {
        super(errorMessage);
    }
}

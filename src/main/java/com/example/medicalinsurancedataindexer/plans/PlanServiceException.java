package com.example.medicalinsurancedataindexer.plans;

import jakarta.servlet.http.HttpServletResponse;

public class PlanServiceException extends RuntimeException {
    public PlanServiceException(String errorMessage) {
        super(errorMessage);
    }
}

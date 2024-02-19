package com.example.medicalinsurancedataindexer.plans;

import com.example.medicalinsurancedataindexer.util.RedisException;
import jakarta.servlet.http.HttpServletResponse;
import org.everit.json.schema.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class PlanControllerAdvice {

    @ResponseBody
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Object planValidationHandler(HttpServletResponse response, ValidationException e) {
        response.setContentType("application/json");
        return new Object() {
            public final String error = "Invalid plan: " + e.getErrorMessage();
        };
    }

    @ResponseBody
    @ExceptionHandler(PlanServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    Object planServiceExceptionHandler(HttpServletResponse response, PlanServiceException e) {
        response.setContentType("application/json");
        return new Object() {
            public final String error = "There was an error processing the plan";
        };
    }

    @ResponseBody
    @ExceptionHandler(RedisException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    Object redisExceptionHandler(HttpServletResponse response, RedisException e) {
        response.setContentType("application/json");
        return new Object() {
            public final String error = "There was an error processing the plan";
        };
    }

    @ResponseBody
    @ExceptionHandler(PlanAvailabilityException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Object planAvailabilityExceptionHandler(HttpServletResponse response, PlanAvailabilityException e) {
        response.setContentType("application/json");
        return new Object() {
            public final String error = "Plan not found";
        };
    }
}

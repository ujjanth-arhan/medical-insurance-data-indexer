package com.example.medicalinsurancedataindexer.config;

import com.example.medicalinsurancedataindexer.security.AccessTokenException;
import com.example.medicalinsurancedataindexer.util.RabbitException;
import com.example.medicalinsurancedataindexer.util.RedisException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GeneralControllerAdvice {
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
    @ExceptionHandler(AccessTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Object accessTokenExceptionHandler(HttpServletResponse response, AccessTokenException e) {
        response.setContentType("application/json");
        return new Object() {
            public final String error = "Access token is invalid";
        };
    }

    @ResponseBody
    @ExceptionHandler(RabbitException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    Object rabbitExceptionHandler(HttpServletResponse response, RabbitException e) {
        response.setContentType("application/json");
        return new Object() {
            public final String error = "There was an error processing the plan";
        };
    }
}

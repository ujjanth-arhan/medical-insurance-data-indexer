package com.example.medicalinsurancedataindexer.util;

public class RedisException extends RuntimeException {
    public RedisException(String message) {
        super(message);
    }
}
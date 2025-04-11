package com.tradelog.backend.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class ValidationErrorResponse {
    private final int status;
    private final String message;
    private final Map<String, String> errors;
    private final LocalDateTime timestamp;

    public ValidationErrorResponse(int status, String message, Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }
} 
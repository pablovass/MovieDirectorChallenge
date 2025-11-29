package com.vallejos.moviedirector.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Map;

/**
 * Global exception handler for the application.
 * This class uses {@link RestControllerAdvice} to centralize exception handling logic
 * across all controllers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link IllegalArgumentException} which is thrown for invalid input,
     * such as a non-numeric threshold.
     *
     * @param ex The caught {@link IllegalArgumentException}.
     * @return A {@link ResponseEntity} with a 400 Bad Request status and a JSON body
     *         containing the error message.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Collections.singletonMap("error", ex.getMessage()));
    }
}

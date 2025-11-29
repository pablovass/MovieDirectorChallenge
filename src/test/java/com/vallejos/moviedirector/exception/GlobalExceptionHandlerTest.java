package com.vallejos.moviedirector.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("GlobalExceptionHandler Unit Tests")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    @DisplayName("should handle IllegalArgumentException and return 400 Bad Request")
    void handleIllegalArgumentException_shouldReturnBadRequest() {

        String errorMessage = "Test error message for illegal argument";
        IllegalArgumentException exception = new IllegalArgumentException(errorMessage);

        ResponseEntity<Map<String, String>> responseEntity = 
            globalExceptionHandler.handleIllegalArgumentException(exception);

        assertNotNull(responseEntity, "ResponseEntity should not be null");
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode(), "Status code should be 400 Bad Request");
        
        Map<String, String> responseBody = responseEntity.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertTrue(responseBody.containsKey("error"), "Response body should contain an 'error' key");
        assertEquals(errorMessage, responseBody.get("error"), "Error message should match the exception message");
    }
}

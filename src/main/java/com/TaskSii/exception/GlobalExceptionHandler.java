package com.TaskSii.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Not Found", e.getMessage());
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<Object> handleInvalidOperation(InvalidOperationException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid operation", e.getMessage());
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        String detailedMessage = e.getMostSpecificCause().getMessage();

        String userMessage;

        if(detailedMessage.contains("not one of the values accepted for Enum class")){
            userMessage = "Unsupported currency";
        }else{
            userMessage = "Invalid request payload";
        }
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid request", userMessage);
    }

    private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String error, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status",status.value());
        body.put("error", error);
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }

}

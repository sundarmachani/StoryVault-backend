package com.daily.my_dairy.custom;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
        ex.getMessage(),
        "Resource not found",
        HttpStatus.NOT_FOUND.value(),
        LocalDateTime.now()
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }
}

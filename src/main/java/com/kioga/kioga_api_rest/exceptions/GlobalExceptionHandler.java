package com.kioga.kioga_api_rest.exceptions;

import java.time.Instant;
import java.util.stream.Collectors;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<GlobalErrorResponse> handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
    return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<GlobalErrorResponse> handleNoResourceFound(NoResourceFoundException ex,
      WebRequest request) {
    return buildErrorResponse("Resource not found", HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<GlobalErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex,
      WebRequest request) {
    String errorMessages = ex.getBindingResult().getFieldErrors()
        .stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .collect(Collectors.joining("; "));
    return buildErrorResponse(errorMessages, HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<GlobalErrorResponse> handleConstraintViolation(ConstraintViolationException ex,
      WebRequest request) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<GlobalErrorResponse> handleBadRequest(BadRequestException ex, WebRequest request) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<GlobalErrorResponse> handleAllUncaughtException(Exception ex, WebRequest request) {
    return buildErrorResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, request);
  }

  private ResponseEntity<GlobalErrorResponse> buildErrorResponse(Exception ex, HttpStatus status, WebRequest request) {
    return buildErrorResponse(ex.getMessage(), status, request);
  }

  private ResponseEntity<GlobalErrorResponse> buildErrorResponse(String message, HttpStatus status,
      WebRequest request) {
    GlobalErrorResponse error = new GlobalErrorResponse(
        Instant.now(),
        status.value(),
        status.getReasonPhrase(),
        message,
        request.getDescription(false).replace("uri=", ""));
    return new ResponseEntity<>(error, status);
  }
}

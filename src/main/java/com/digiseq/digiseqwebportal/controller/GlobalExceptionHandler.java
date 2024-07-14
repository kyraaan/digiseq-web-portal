package com.digiseq.digiseqwebportal.controller;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.digiseq.digiseqwebportal.exception.ClientOrgNotFoundException;
import com.digiseq.digiseqwebportal.exception.PersonnelNotFoundException;
import com.digiseq.digiseqwebportal.exception.model.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  private static final String DEFAULT_VALIDATION_MESSAGE = "Validation error";

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleException(RuntimeException e) {
    log.error("Handling unknown exception", e);
    ErrorResponse errorResponse =
        new ErrorResponse(INTERNAL_SERVER_ERROR.value(), "Unexpected error");
    return new ResponseEntity<>(errorResponse, INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(ClientOrgNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleException(ClientOrgNotFoundException e) {
    log.error("Handling ClientOrgNotFoundException: {}", e.getMessage());
    ErrorResponse errorResponse = new ErrorResponse(NOT_FOUND.value(), e.getMessage());
    return new ResponseEntity<>(errorResponse, NOT_FOUND);
  }

  @ExceptionHandler(PersonnelNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleException(PersonnelNotFoundException e) {
    log.error("Handling PersonnelNotFoundException: {}", e.getMessage());
    ErrorResponse errorResponse = new ErrorResponse(NOT_FOUND.value(), e.getMessage());
    return new ResponseEntity<>(errorResponse, NOT_FOUND);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e) {
    log.error("Handling MethodArgumentNotValidException: {}", e.getMessage());

    Map<String, String> errors =
        e.getBindingResult().getFieldErrors().stream()
            .collect(toMap(FieldError::getField, GlobalExceptionHandler::getDefaultMessage));

    ErrorResponse errorResponse =
        new ErrorResponse(BAD_REQUEST.value(), DEFAULT_VALIDATION_MESSAGE, errors);
    return new ResponseEntity<>(errorResponse, BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleException(ConstraintViolationException e) {
    log.error("Handling ConstraintViolationException: {}", e.getMessage());

    String message =
        e.getConstraintViolations().stream()
            .findFirst()
            .map(ConstraintViolation::getMessage)
            .orElse(DEFAULT_VALIDATION_MESSAGE);
    ErrorResponse errorResponse = new ErrorResponse(BAD_REQUEST.value(), message);
    return new ResponseEntity<>(errorResponse, BAD_REQUEST);
  }

  private static String getDefaultMessage(FieldError fieldError) {
    return ofNullable(fieldError.getDefaultMessage()).orElse(DEFAULT_VALIDATION_MESSAGE);
  }
}

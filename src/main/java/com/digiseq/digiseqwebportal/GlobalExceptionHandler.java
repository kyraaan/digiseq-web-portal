package com.digiseq.digiseqwebportal;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.digiseq.digiseqwebportal.clientorg.exception.ClientOrgNotFoundException;
import com.digiseq.digiseqwebportal.exception.DigiseqBaseException;
import com.digiseq.digiseqwebportal.exception.model.ErrorResponse;
import com.digiseq.digiseqwebportal.personnel.exception.PersonnelNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

  @ExceptionHandler(DigiseqBaseException.class)
  public ResponseEntity<ErrorResponse> handleException(DigiseqBaseException e) {
    log.error("Handling DigiseqBaseException", e);
    ErrorResponse errorResponse = new ErrorResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage());
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

    Map<String, List<String>> errors =
            e.getBindingResult().getFieldErrors().stream().
                    collect(groupingBy(FieldError::getField, mapping(GlobalExceptionHandler::getDefaultMessage, toList())));

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

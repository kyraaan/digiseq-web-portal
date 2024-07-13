package com.digiseq.digiseqwebportal.exception.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

@JsonInclude(NON_NULL)
public record ErrorResponse(int statusCode, String message, Map<String, String> errors) {
  public ErrorResponse(int statusCode, String message) {
    this(statusCode, message, null);
  }
}

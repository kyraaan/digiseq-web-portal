package com.digiseq.digiseqwebportal.exception;

public class DigiseqBaseException extends RuntimeException {

  public DigiseqBaseException(String message) {
    super(message);
  }

  public DigiseqBaseException(String message, Exception e) {
    super(message, e);
  }
}

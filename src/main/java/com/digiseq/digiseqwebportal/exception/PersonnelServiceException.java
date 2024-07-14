package com.digiseq.digiseqwebportal.exception;

public class PersonnelServiceException extends RuntimeException {

  public PersonnelServiceException(String message, Exception e) {
    super(message, e);
  }
}

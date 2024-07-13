package com.digiseq.digiseqwebportal.exception;

public class ClientOrgServiceException extends RuntimeException {

  public ClientOrgServiceException(String message, Exception e) {
    super(message, e);
  }
}

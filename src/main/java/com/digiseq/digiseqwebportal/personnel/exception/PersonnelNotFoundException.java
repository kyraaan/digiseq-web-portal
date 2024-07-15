package com.digiseq.digiseqwebportal.personnel.exception;

import com.digiseq.digiseqwebportal.exception.DigiseqBaseException;

public class PersonnelNotFoundException extends DigiseqBaseException {

  public PersonnelNotFoundException(String message) {
    super(message);
  }
}

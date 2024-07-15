package com.digiseq.digiseqwebportal.personnel.controller.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PersonnelIdValidator implements ConstraintValidator<ValidPersonnelId, String> {
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    try {
      Long.parseLong(value);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}

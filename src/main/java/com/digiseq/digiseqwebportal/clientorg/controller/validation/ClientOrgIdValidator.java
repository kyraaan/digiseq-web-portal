package com.digiseq.digiseqwebportal.clientorg.controller.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ClientOrgIdValidator implements ConstraintValidator<ValidClientOrgId, String> {
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

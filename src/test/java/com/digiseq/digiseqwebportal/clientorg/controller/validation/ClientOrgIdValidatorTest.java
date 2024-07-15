package com.digiseq.digiseqwebportal.clientorg.controller.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ClientOrgIdValidatorTest {
  ClientOrgIdValidator clientOrgIdValidator = new ClientOrgIdValidator();

  @Test
  void shouldReturnTrue_givenValidClientOrgId() {
    boolean isValid = clientOrgIdValidator.isValid("1234", null);

    assertTrue(isValid);
  }

  @ParameterizedTest
  @ValueSource(strings = "invalid")
  @NullAndEmptySource
  void shouldReturnFalse_givenInvalidClientOrgId(String clientOrgId) {
    boolean isValid = clientOrgIdValidator.isValid(clientOrgId, null);

    assertFalse(isValid);
  }
}

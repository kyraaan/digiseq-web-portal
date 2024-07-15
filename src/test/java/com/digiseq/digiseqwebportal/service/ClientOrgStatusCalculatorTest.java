package com.digiseq.digiseqwebportal.service;

import static com.digiseq.digiseqwebportal.model.ClientOrgStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.digiseq.digiseqwebportal.model.ClientOrgStatus;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class ClientOrgStatusCalculatorTest {
  private static final int DAYS_UNTIL_EXPIRING_SOON = 7;
  private static final LocalDate CURRENT_DATE = LocalDate.of(2020, 1, 15);

  @Mock private DateProvider dateProvider;

  private ClientOrgStatusCalculator clientOrgStatusCalculator;

  @BeforeEach
  void setup() {
    given(dateProvider.getCurrentDate()).willReturn(CURRENT_DATE);

    clientOrgStatusCalculator =
        new ClientOrgStatusCalculator(dateProvider, DAYS_UNTIL_EXPIRING_SOON);
  }

  @Test
  void shouldReturnExpired_givenExpiryDate() {
    LocalDate expiryDate = CURRENT_DATE.minusDays(1);

    ClientOrgStatus status = clientOrgStatusCalculator.calculateStatus(expiryDate);

    assertThat(status).isEqualTo(EXPIRED);
  }

  @Test
  void shouldReturnExpiringSoon_givenCurrentDate_IsDuringExpiringSoonPeriod() {
    LocalDate expiryDate = CURRENT_DATE.plusDays(DAYS_UNTIL_EXPIRING_SOON);

    ClientOrgStatus status = clientOrgStatusCalculator.calculateStatus(expiryDate);

    assertThat(status).isEqualTo(EXPIRING_SOON);
  }

  @Test
  void shouldReturnActive_givenCurrentDateIsBeforeExpiryDateAndBeforeExpiringSoonDate() {
    LocalDate expiryDate = CURRENT_DATE.plusDays(DAYS_UNTIL_EXPIRING_SOON + 1);

    ClientOrgStatus status = clientOrgStatusCalculator.calculateStatus(expiryDate);

    assertThat(status).isEqualTo(ACTIVE);
  }
}

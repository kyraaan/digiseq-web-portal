package com.digiseq.digiseqwebportal.service;

import static java.time.LocalDate.ofInstant;
import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DateProviderTest {

  private DateProvider dateProvider;

  @BeforeEach
  void setup() {
    dateProvider = new DateProvider(Clock.systemDefaultZone());
  }

  @Test
  void shouldGetCurrentDate() {
    LocalDate localDate = ofInstant(Instant.now(), UTC);

    LocalDate actualDate = dateProvider.getCurrentDate();

    assertThat(actualDate).isEqualTo(localDate);
  }
}

package com.digiseq.digiseqwebportal.util;

import java.time.Clock;
import java.time.LocalDate;

public class DateProvider {
  private final Clock clock;

  public DateProvider(Clock clock) {
    this.clock = clock;
  }

  public LocalDate getCurrentDate() {
    return LocalDate.now(clock);
  }
}

package com.digiseq.digiseqwebportal.clientorg.service;

import static com.digiseq.digiseqwebportal.clientorg.model.ClientOrgStatus.ACTIVE;
import static com.digiseq.digiseqwebportal.clientorg.model.ClientOrgStatus.EXPIRED;
import static com.digiseq.digiseqwebportal.clientorg.model.ClientOrgStatus.EXPIRING_SOON;
import static java.time.temporal.ChronoUnit.DAYS;

import com.digiseq.digiseqwebportal.clientorg.model.ClientOrgStatus;
import com.digiseq.digiseqwebportal.util.DateProvider;
import java.time.LocalDate;

public class ClientOrgStatusCalculator {
  private final DateProvider dateProvider;
  private final long daysUntilExpiringSoon;

  public ClientOrgStatusCalculator(DateProvider dateProvider, long daysUntilExpiringSoon) {
    this.dateProvider = dateProvider;
    this.daysUntilExpiringSoon = daysUntilExpiringSoon;
  }

  public ClientOrgStatus calculateStatus(LocalDate expiryDate) {
    LocalDate currentDate = dateProvider.getCurrentDate();
    long daysUntilExpired = DAYS.between(currentDate, expiryDate);

    if (daysUntilExpired <= 0) {
      return EXPIRED;
    }
    if (daysUntilExpired <= daysUntilExpiringSoon) {
      return EXPIRING_SOON;
    }
    return ACTIVE;
  }
}

package com.digiseq.digiseqwebportal.model;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record ClientOrg(
    Long clientOrgId,
    String name,
    LocalDate registeredDate,
    LocalDate expiryDate,
    boolean isEnabled) {}

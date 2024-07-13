package com.digiseq.digiseqwebportal.model;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record ClientOrg(
    Long clientOrgId,
    String name,
    LocalDate registeredDate,
    LocalDate expiryDate,
    boolean isEnabled,
    List<Personnel> personnel) {}

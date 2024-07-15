package com.digiseq.digiseqwebportal.model;

import java.time.LocalDate;
import lombok.Builder;

@Builder(toBuilder = true)
public record ClientOrg(
    Long clientOrgId,
    String name,
    LocalDate registeredDate,
    LocalDate expiryDate,
    ClientOrgStatus status) {}

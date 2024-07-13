package com.digiseq.digiseqwebportal.controller.model;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record ClientOrgResponse(
    Long clientOrgId,
    String name,
    LocalDate registeredDate,
    LocalDate expiryDate,
    boolean isEnabled,
    List<PersonnelResponse> personnel) {}

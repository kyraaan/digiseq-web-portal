package com.digiseq.digiseqwebportal.controller.model.response;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record ClientOrgResponse(
    Long clientOrgId,
    String name,
    LocalDate registeredDate,
    LocalDate expiryDate,
    boolean isEnabled) {}

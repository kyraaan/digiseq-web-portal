package com.digiseq.digiseqwebportal.clientorg.controller.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record AddClientOrgRequest(
    @NotEmpty(message = "name cannot be empty") String name,
    @NotNull(message = "registeredDate cannot be null") LocalDate registeredDate,
    @NotNull(message = "expiryDate cannot be null") LocalDate expiryDate) {}

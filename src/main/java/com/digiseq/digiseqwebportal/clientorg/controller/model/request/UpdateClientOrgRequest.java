package com.digiseq.digiseqwebportal.clientorg.controller.model.request;

import jakarta.annotation.Nullable;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record UpdateClientOrgRequest(
    @Nullable String name, @Nullable LocalDate registeredDate, @Nullable LocalDate expiryDate) {}

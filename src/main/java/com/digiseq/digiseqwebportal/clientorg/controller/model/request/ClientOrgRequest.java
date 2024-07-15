package com.digiseq.digiseqwebportal.clientorg.controller.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
public record ClientOrgRequest(
    @NotBlank @Size(min = 3, max = 20, message = "must be between 3 and 20 characters") String name,
    @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate registeredDate,
    @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate expiryDate) {}

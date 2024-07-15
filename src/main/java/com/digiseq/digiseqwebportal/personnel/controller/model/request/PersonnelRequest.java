package com.digiseq.digiseqwebportal.personnel.controller.model.request;

import com.digiseq.digiseqwebportal.clientorg.controller.validation.ValidId;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record PersonnelRequest(
    @NotBlank @Size(min = 3, max = 20, message = "must be between 3 and 20 characters")
        String username,
    @NotBlank @Size(max = 50, message = "must not exceed 50 characters") String firstName,
    @NotBlank @Size(max = 50, message = "must not exceed 50 characters") String lastName,
    @NotBlank @Size(min = 8, max = 20, message = "must be between 8-20 characters") String password,
    @NotBlank @Email(message = "must be a valid email format") String email,
    @NotBlank String phoneNumber,
    @ValidId String clientOrgId) {}

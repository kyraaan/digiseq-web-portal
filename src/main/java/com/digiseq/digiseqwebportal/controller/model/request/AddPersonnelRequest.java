package com.digiseq.digiseqwebportal.controller.model.request;

import com.digiseq.digiseqwebportal.controller.validation.ValidClientOrgId;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record AddPersonnelRequest(
    @NotEmpty(message = "username cannot be empty") String username,
    @NotEmpty(message = "firstName cannot be empty") String firstName,
    @NotEmpty(message = "lastName cannot be empty") String lastName,
    @NotEmpty(message = "password cannot be empty") String password,
    @NotEmpty(message = "email cannot be empty") String email,
    @NotEmpty(message = "phoneNumber cannot be empty") String phoneNumber,
    @ValidClientOrgId Long clientOrgId) {}

package com.digiseq.digiseqwebportal.controller.model;

import lombok.Builder;

@Builder
public record PersonnelResponse(
    Long personnelId,
    String username,
    String firstName,
    String lastName,
    String email,
    String phoneNumber) {}

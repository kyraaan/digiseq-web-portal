package com.digiseq.digiseqwebportal.model;

import lombok.Builder;

@Builder
public record Personnel(
    Long personnelId,
    String username,
    String firstName,
    String lastName,
    String password,
    String email,
    String phoneNumber,
    Long clientOrgId) {}

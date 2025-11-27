package com.careerpath.application.dto;

public record SyncUserRequest(
        String externalId,
        String email,
        boolean emailVerified
) {}

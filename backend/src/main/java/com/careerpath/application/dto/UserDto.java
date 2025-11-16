package com.careerpath.application.dto;

import com.careerpath.domain.modelOld.enums.UserRole;

import java.util.UUID;

public record UserDto (
    UUID id,
    String email,
    String passwordHash,
    UserRole role,
    ProfileDto profile
) { }

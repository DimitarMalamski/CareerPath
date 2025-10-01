package com.careerpath.dto;

import com.careerpath.model.enums.UserRole;

import java.util.UUID;

public record UserDto (
    UUID id,
    String email,
    String passwordHash,
    UserRole role,
    ProfileDto profile
) { }

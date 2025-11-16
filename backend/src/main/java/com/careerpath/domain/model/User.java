package com.careerpath.domain.model;

import com.careerpath.domain.model.enums.UserRole;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class User {

    private UUID id;
    private String email;
    private String passwordHash;
    private UserRole role;

    private OffsetDateTime emailVerifiedAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}

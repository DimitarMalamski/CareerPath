package com.careerpath.infrastructure.persistence.jpa.mapper;

import com.careerpath.domain.model.User;
import com.careerpath.domain.model.UserSkill;
import com.careerpath.infrastructure.persistence.jpa.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

public class UserEntityMapper {

    public static User toDomain(UserEntity entity, List<UserSkill> userSkills) {
        if (entity == null) return null;

        return User.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .passwordHash(entity.getPasswordHash())
                .role(entity.getRole())
                .emailVerifiedAt(entity.getEmailVerifiedAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .skills(userSkills != null ? userSkills : new ArrayList<>())
                .build();
    }

    public static UserEntity toEntity(User user) {
        if (user == null) return null;

        return UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .passwordHash(user.getPasswordHash())
                .role(user.getRole())
                .emailVerifiedAt(user.getEmailVerifiedAt())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}

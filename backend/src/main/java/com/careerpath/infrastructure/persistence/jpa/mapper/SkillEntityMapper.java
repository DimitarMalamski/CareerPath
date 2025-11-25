package com.careerpath.infrastructure.persistence.jpa.mapper;

import com.careerpath.domain.model.Skill;
import com.careerpath.infrastructure.persistence.jpa.entity.SkillEntity;

public class SkillEntityMapper {
    public static Skill toDomain(SkillEntity entity) {
        return Skill.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}

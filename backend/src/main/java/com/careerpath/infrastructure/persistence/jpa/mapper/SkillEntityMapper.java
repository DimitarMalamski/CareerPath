package com.careerpath.infrastructure.persistence.jpa.mapper;

import com.careerpath.domain.model.Skill;
import com.careerpath.infrastructure.persistence.jpa.entity.SkillEntity;

public class SkillEntityMapper {

    private SkillEntityMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Skill toDomain(SkillEntity entity) {
        return Skill.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public static SkillEntity toEntity(Skill skill) {
        SkillEntity entity = new SkillEntity();
        entity.setId(skill.getId());
        entity.setName(skill.getName());
        return entity;
    }
}

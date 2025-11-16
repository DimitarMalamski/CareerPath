package com.careerpath.infrastructure.persistence.jpa.mapper;

import com.careerpath.domain.model.UserSkill;
import com.careerpath.infrastructure.persistence.jpa.entity.UserSkillEntity;
import com.careerpath.infrastructure.persistence.jpa.entity.UserSkillIdEntity;

public class UserSkillEntityMapper {

    public static UserSkill toDomain(UserSkillEntity entity, String skillName) {
        return UserSkill.builder()
                .userId(entity.getId().getUserId())
                .skillId(entity.getId().getSkillId())
                .skillName(skillName)
                .level(entity.getLevel())
                .build();
    }

    public static UserSkillEntity toEntity(UserSkill userSkill) {
        return UserSkillEntity.builder()
                .id(new UserSkillIdEntity(userSkill.getUserId(), userSkill.getSkillId()))
                .level(userSkill.getLevel())
                .build();
    }
}

package com.careerpath.infrastructure.persistence.jpa.mapper;

import com.careerpath.domain.model.JobSkill;
import com.careerpath.infrastructure.persistence.jpa.entity.JobSkillEntity;

public class JobSkillEntityMapper {
    public static JobSkill toDomain(JobSkillEntity entity) {
        return JobSkill.builder()
                .jobId(entity.getJob().getId())
                .skillId(entity.getSkill().getId())
                .build();
    }
}

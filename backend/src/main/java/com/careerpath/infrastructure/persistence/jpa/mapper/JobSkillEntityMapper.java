package com.careerpath.infrastructure.persistence.jpa.mapper;

import com.careerpath.domain.model.JobSkill;
import com.careerpath.infrastructure.persistence.jpa.entity.JobSkillEntity;

public class JobSkillEntityMapper {

    private JobSkillEntityMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static JobSkill toDomain(JobSkillEntity entity) {
        return JobSkill.builder()
                .jobId(entity.getJob().getId())
                .skillId(entity.getSkill().getId())
                .build();
    }
}

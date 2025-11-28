package com.careerpath.infrastructure.persistence.jpa.mapper;

import com.careerpath.domain.model.JobListing;
import com.careerpath.domain.model.Skill;
import com.careerpath.infrastructure.persistence.jpa.entity.JobListingEntity;
import com.careerpath.infrastructure.persistence.jpa.entity.JobSkillEntity;

import java.util.List;

public class JobListingEntityMapper {

    private JobListingEntityMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static JobListing toDomain(JobListingEntity entity) {
        List<Skill> skills =
                entity.getJobSkills() == null ? List.of() :
                entity.getJobSkills()
                    .stream()
                    .map(JobSkillEntity::getSkill)
                    .map(se -> Skill.builder()
                            .id(se.getId())
                            .name(se.getName())
                            .build()
                    )
                .toList();

        return JobListing.builder()
                .id(entity.getId())
                .recruiterId(entity.getRecruiterId())
                .title(entity.getTitle())
                .company(entity.getCompany())
                .location(entity.getLocation())
                .type(entity.getType())
                .stackSummary(entity.getStackSummary())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .expiresAt(entity.getExpiresAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .skills(skills)
                .build();
    }
}

package com.careerpath.infrastructure.persistence.jpa.mapper;

import com.careerpath.domain.model.JobListing;
import com.careerpath.infrastructure.persistence.jpa.entity.JobListingEntity;

public class JobListingEntityMapper {
    public static JobListing toDomain(JobListingEntity entity) {
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
                .build();
    }
}

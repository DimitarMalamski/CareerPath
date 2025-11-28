package com.careerpath.application.mapper;

import com.careerpath.application.dto.JobListingDto;
import com.careerpath.domain.model.JobListing;
import com.careerpath.domain.model.Skill;

public class JobListingDtoMapper {

    private JobListingDtoMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static JobListingDto toDto(JobListing job) {
        return new JobListingDto(
                job.getId(),
                job.getTitle(),
                job.getCompany(),
                job.getLocation(),
                job.getStackSummary(),
                job.getType(),
                job.getStatus(),
                job.getExpiresAt(),
                job.getSkills().stream()
                        .map(Skill::getName)
                        .toList()
        );
    }
}

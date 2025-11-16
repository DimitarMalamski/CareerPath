package com.careerpath.application.mapper;

import com.careerpath.application.dto.JobListingDto;
import com.careerpath.domain.model.JobListing;

import java.util.stream.Collectors;

public class JobListingDtoMapper {
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
                        .map(skill -> skill.getName())
                        .collect(Collectors.toList())
        );
    }
}

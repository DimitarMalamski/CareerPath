package com.careerpath.mapper;

import com.careerpath.dto.JobListingDto;
import com.careerpath.model.JobListing;
import com.careerpath.model.JobSkill;

import java.util.List;

public class JobListingMapper {
    public static JobListingDto toDto(JobListing jobListing, List<JobSkill> jobSkill) {
        List<String> skills = jobSkill.stream()
                .map(js -> js.getSkill().getName())
                .toList();

        return new JobListingDto(
                jobListing.getId(),
                jobListing.getTitle(),
                jobListing.getCompany(),
                jobListing.getLocation(),
                jobListing.getStackSummary(),
                jobListing.getType(),
                jobListing.getStatus(),
                jobListing.getExpiresAt(),
                skills
        );
    }
}

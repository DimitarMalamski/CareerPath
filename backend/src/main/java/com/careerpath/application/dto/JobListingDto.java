package com.careerpath.application.dto;

import com.careerpath.domain.model.enums.JobType;
import com.careerpath.domain.model.enums.JobStatus;

import java.util.UUID;
import java.time.LocalDate;
import java.util.List;

public record JobListingDto(
        UUID id,
        String title,
        String company,
        String location,
        String stackSummary,
        JobType type,
        JobStatus status,
        LocalDate expiresAt,
        List<String> skills
) { }

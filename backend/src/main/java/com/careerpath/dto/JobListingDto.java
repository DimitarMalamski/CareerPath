package com.careerpath.dto;

import com.careerpath.model.enums.JobType;
import com.careerpath.model.enums.JobStatus;

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

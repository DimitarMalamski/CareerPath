package com.careerpath.application.dto;

public record JobMatchResultDto (
        String jobListingId,
        double score,
        String explanation
) {}

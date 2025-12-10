package com.careerpath.application.dto;

import java.util.List;

public record JobDetailsDto(
        String id,
        String title,
        String company,
        String location,
        String type,
        String description,
        String stackSummary,
        double finalScore,
        String aiExplanation,
        String createdAt,
        String updatedAt,
        String expiresAt,
        List<String> skills,
        List<String> matchedSkills,
        List<String> missingSkills,
        String applyUrl
) {}

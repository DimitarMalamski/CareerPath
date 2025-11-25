package com.careerpath.application.dto;

import java.util.List;

public record JobRecommendationDto(
        String id,
        String title,
        String company,
        String location,
        String type,
        List<String> skills,
        String description,
        double finalScore,
        String aiExplanation,
        String createdAt,

        List<String> matchedSkills,
        List<String> missingSkills
) {}

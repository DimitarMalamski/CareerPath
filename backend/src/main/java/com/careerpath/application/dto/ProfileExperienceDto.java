package com.careerpath.application.dto;

public record ProfileExperienceDto(
        String id,
        String company,
        String title,
        String employmentType,
        String location,
        String startDate,
        String endDate,
        boolean isCurrent,
        String description
) { }

package com.careerpath.application.dto;

public record ProfileExperienceDto(
        Long id,
        String company,
        String title,
        String employmentType,
        String location,
        String startDate,
        String endDate,
        boolean isCurrent,
        String description
) { }

package com.careerpath.application.dto;

public record CreateJobListingDto (
    String title,
    String company,
    String location
) {}

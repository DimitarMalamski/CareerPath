package com.careerpath.application.dto;

import java.util.List;

public record CreateJobListingDto (
    String title,
    String company,
    String location,

    String type,
    List<String> skills,
    String description
) {}

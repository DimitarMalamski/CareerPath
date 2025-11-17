package com.careerpath.application.dto;

import java.util.List;

public record ProfileDto(
        Long id,
        String fullName,
        String headline,
        String about,
        String location,
        List<String> skills,
        List<String> experiences
) { }

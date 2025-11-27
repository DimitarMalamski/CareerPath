package com.careerpath.application.dto;

import java.util.List;

public record ProfileDto(
        String fullName,
        String headline,
        String about,
        String location,
        List<ProfileSkillDto> skills,
        List<ProfileExperienceDto> experiences,
        boolean aiOptIn
) { }

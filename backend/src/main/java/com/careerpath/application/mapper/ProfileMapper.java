package com.careerpath.application.mapper;

import com.careerpath.application.dto.*;
import com.careerpath.domain.model.*;

import java.util.List;
import java.util.UUID;

public class ProfileMapper {

    private ProfileMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static ProfileDto toDto(Profile domain) {
        return new ProfileDto(
                domain.getFullName(),
                domain.getHeadline(),
                domain.getAbout(),
                domain.getLocation(),
                domain.getSkills() == null
                        ? List.of()
                        : domain.getSkills().stream()
                        .map(ProfileMapper::toDto)
                        .toList(),
                domain.getExperiences() == null
                        ? List.of()
                        : domain.getExperiences().stream()
                        .map(ProfileMapper::toDto)
                        .toList(),
                domain.isAiOptIn()
        );
    }

    public static ProfileSkillDto toDto(ProfileSkill domain) {
        return new ProfileSkillDto(
                domain.getId(),
                domain.getName(),
                domain.getLevel()
        );
    }

    public static ProfileExperienceDto toDto(ProfileExperience domain) {
        return new ProfileExperienceDto(
                domain.getId(),
                domain.getCompany(),
                domain.getTitle(),
                domain.getEmploymentType(),
                domain.getLocation(),
                domain.getStartDate(),
                domain.getEndDate(),
                domain.isCurrent(),
                domain.getDescription()
        );
    }

    public static Profile toDomain(String userId, ProfileDto dto) {
        return Profile.builder()
                .userId(userId)
                .fullName(dto.fullName())
                .headline(dto.headline())
                .about(dto.about())
                .location(dto.location())
                .skills(dto.skills() != null ? dto.skills().stream()
                        .map(ProfileMapper::toDomain)
                        .toList() : List.of())
                .experiences(dto.experiences() != null ? dto.experiences().stream()
                        .map(ProfileMapper::toDomain)
                        .toList() : List.of())
                .aiOptIn(dto.aiOptIn())
                .build();
    }

    public static ProfileSkill toDomain(ProfileSkillDto dto) {
        return ProfileSkill.builder()
                .id(dto.id() != null ? dto.id() : UUID.randomUUID().toString())
                .name(dto.name())
                .level(dto.level())
                .build();
    }

    public static ProfileExperience toDomain(ProfileExperienceDto dto) {
        return ProfileExperience.builder()
                .id(dto.id() != null ? dto.id() : UUID.randomUUID().toString())
                .company(dto.company())
                .title(dto.title())
                .employmentType(dto.employmentType())
                .location(dto.location())
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .isCurrent(dto.isCurrent())
                .description(dto.description())
                .build();
    }
}

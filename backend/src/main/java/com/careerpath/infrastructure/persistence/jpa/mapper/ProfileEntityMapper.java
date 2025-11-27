package com.careerpath.infrastructure.persistence.jpa.mapper;

import com.careerpath.domain.model.Profile;
import com.careerpath.domain.model.ProfileExperience;
import com.careerpath.domain.model.ProfileSkill;
import com.careerpath.infrastructure.persistence.jpa.entity.*;

import java.util.List;

public class ProfileEntityMapper {

    public static Profile toDomain(ProfileEntity entity) {
        ProfileDataEmbeddable data = entity.getData();

        return Profile.builder()
                .userId(entity.getUserId())
                .fullName(data.getFullName())
                .headline(data.getHeadline())
                .about(data.getAbout())
                .location(data.getLocation())
                .skills(toDomainSkills(data.getSkills()))
                .experiences(toDomainExperiences(data.getExperiences()))
                .aiOptIn(entity.isAiOptIn())
                .build();
    }

    private static List<ProfileSkill> toDomainSkills(List<ProfileSkillEmbeddable> list) {
        if (list == null) return List.of();

        return list.stream()
                .map(s -> ProfileSkill.builder()
                        .id(s.getId())
                        .name(s.getName())
                        .level(s.getLevel())
                        .build())
                .toList();
    }

    private static List<ProfileExperience> toDomainExperiences(List<ProfileExperienceEmbeddable> list) {
        if (list == null) return List.of();

        return list.stream()
                .map(e -> ProfileExperience.builder()
                        .id(e.getId())
                        .company(e.getCompany())
                        .title(e.getTitle())
                        .employmentType(e.getEmploymentType())
                        .location(e.getLocation())
                        .startDate(e.getStartDate())
                        .endDate(e.getEndDate())
                        .isCurrent(e.isCurrent())
                        .description(e.getDescription())
                        .build())
                .toList();
    }

    // -------------------------------------
    // Domain → JPA
    // -------------------------------------
    public static ProfileEntity toEntity(Profile profile) {

        ProfileDataEmbeddable data = ProfileDataEmbeddable.builder()
                .fullName(profile.getFullName())
                .headline(profile.getHeadline())
                .about(profile.getAbout())
                .location(profile.getLocation())
                .skills(toEmbeddableSkills(profile.getSkills()))
                .experiences(toEmbeddableExperiences(profile.getExperiences()))
                .build();

        return ProfileEntity.builder()
                .userId(profile.getUserId())
                .data(data)
                .aiOptIn(profile.isAiOptIn())
                .build();
    }

    private static List<ProfileSkillEmbeddable> toEmbeddableSkills(List<ProfileSkill> list) {
        if (list == null) return List.of();

        return list.stream()
                .map(s -> ProfileSkillEmbeddable.builder()
                        .id(s.getId())
                        .name(s.getName())
                        .level(s.getLevel())
                        .build())
                .toList();
    }

    private static List<ProfileExperienceEmbeddable> toEmbeddableExperiences(List<ProfileExperience> list) {
        if (list == null) return List.of();

        return list.stream()
                .map(e -> ProfileExperienceEmbeddable.builder()
                        .id(e.getId())
                        .company(e.getCompany())
                        .title(e.getTitle())
                        .employmentType(e.getEmploymentType())
                        .location(e.getLocation())
                        .startDate(e.getStartDate())
                        .endDate(e.getEndDate())
                        .current(e.isCurrent())
                        .description(e.getDescription())
                        .build())
                .toList();
    }
}

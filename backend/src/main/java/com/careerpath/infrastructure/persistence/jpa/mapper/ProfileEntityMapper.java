package com.careerpath.infrastructure.persistence.jpa.mapper;

import com.careerpath.domain.model.Profile;
import com.careerpath.domain.model.ProfileExperience;
import com.careerpath.domain.model.ProfileSkill;
import com.careerpath.infrastructure.persistence.jpa.entity.ProfileDataEmbeddable;
import com.careerpath.infrastructure.persistence.jpa.entity.ProfileEntity;
import com.careerpath.infrastructure.persistence.jpa.entity.ProfileExperienceEmbeddable;
import com.careerpath.infrastructure.persistence.jpa.entity.ProfileSkillEmbeddable;

import java.util.ArrayList;
import java.util.List;

public class ProfileEntityMapper {

    public static Profile toDomain(ProfileEntity entity) {

        return Profile.builder()
                .userId(entity.getUserId())
                .fullName(entity.getData().getFullName())
                .headline(entity.getData().getHeadline())
                .about(entity.getData().getAbout())
                .location(entity.getData().getLocation())
                .experiences(mapExperiences(entity.getData().getExperiences()))
                .skills(mapSkills(entity.getData().getSkills()))
                .build();
    }

    public static ProfileEntity toEntity(Profile profile) {
        return ProfileEntity.builder()
                .userId(profile.getUserId())
                .data(ProfileDataEmbeddable.builder()
                        .fullName(profile.getFullName())
                        .headline(profile.getHeadline())
                        .about(profile.getAbout())
                        .location(profile.getLocation())
                        .experiences(mapExperienceToEmbeddable(profile.getExperiences()))
                        .skills(mapSkillsToEmbeddable(profile.getSkills()))
                        .build()
                )
                .aiOptIn(false)
                .build();
    }

    private static List<ProfileSkillEmbeddable> mapSkillsToEmbeddable(List<ProfileSkill> list) {
        if (list == null) return List.of();

        List<ProfileSkillEmbeddable> result = new ArrayList<>();

        for (ProfileSkill s : list) {
            result.add(ProfileSkillEmbeddable.builder()
                    .name(s.getName())
                    .level(s.getLevel())
                    .build()
            );
        }

        return result;
    }

    private static List<ProfileExperienceEmbeddable> mapExperienceToEmbeddable(List<ProfileExperience> list) {
        if (list == null) return List.of();

        List<ProfileExperienceEmbeddable> result = new ArrayList<>();

        for (ProfileExperience e : list) {
            result.add(ProfileExperienceEmbeddable.builder()
                    .title(e.getTitle())
                    .company(e.getCompany())
                    .startDate(e.getStartDate())
                    .endDate(e.getEndDate())
                    .description(e.getDescription())
                    .build()
            );
        }

        return result;
    }

    private static List<ProfileExperience> mapExperiences(List<ProfileExperienceEmbeddable> list) {
        if (list == null) return List.of();

        List<ProfileExperience> result = new ArrayList<>();

        for (ProfileExperienceEmbeddable e : list) {
            result.add(ProfileExperience.builder()
                    .title(e.getTitle())
                    .company(e.getCompany())
                    .startDate(e.getStartDate())
                    .endDate(e.getEndDate())
                    .description(e.getDescription())
                    .build()
            );
        }

        return result;
    }

    private static List<ProfileSkill> mapSkills(List<ProfileSkillEmbeddable> list) {
        if (list == null) return List.of();

        List<ProfileSkill> result = new ArrayList<>();

        for (ProfileSkillEmbeddable s : list) {
            result.add(ProfileSkill.builder()
                    .id(null)
                    .name(s.getName())
                    .level(s.getLevel())
                    .build()
            );
        }

        return result;
    }
}

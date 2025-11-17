package com.careerpath.infrastructure.persistence.jpa.mapper;

import com.careerpath.domain.model.Profile;
import com.careerpath.domain.model.ProfileExperience;
import com.careerpath.domain.model.ProfileSkill;
import com.careerpath.infrastructure.persistence.jpa.entity.ProfileEntity;
import com.careerpath.infrastructure.persistence.jpa.entity.ProfileExperienceEmbeddable;
import com.careerpath.infrastructure.persistence.jpa.entity.ProfileSkillEmbeddable;

import java.util.ArrayList;
import java.util.List;

public class ProfileEntityMapper {

    public static Profile toDomain(ProfileEntity entity) {

        return Profile.builder()
                .id(entity.getUserId())
                .fullName(entity.getFullName())
                .headline(entity.getHeadline())
                .about(entity.getAbout())
                .location(entity.getLocation())
                .experiences(mapExperiences(entity.getExperiences()))
                .skills(mapSkills(entity.getSkills()))
                .build();
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

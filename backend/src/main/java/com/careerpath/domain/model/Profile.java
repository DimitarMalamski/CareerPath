package com.careerpath.domain.model;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {
    private String userId;

    private String fullName;
    private String headline;
    private String about;
    private String location;

    private List<ProfileSkill> skills;
    private List<ProfileExperience> experiences;

    private boolean aiOptIn;
}

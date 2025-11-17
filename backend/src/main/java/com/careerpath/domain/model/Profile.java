package com.careerpath.domain.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {
    private Long id;

    private String fullName;
    private String headline;
    private String about;
    private String location;

    private List<ProfileSkill> skills;
    private List<ProfileExperience> experiences;
}

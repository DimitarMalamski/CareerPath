package com.careerpath.infrastructure.persistence.jpa.entity;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDataEmbeddable {

    private String fullName;
    private String headline;
    private String about;
    private String location;

    private List<ProfileSkillEmbeddable> skills;
    private List<ProfileExperienceEmbeddable> experiences;
}

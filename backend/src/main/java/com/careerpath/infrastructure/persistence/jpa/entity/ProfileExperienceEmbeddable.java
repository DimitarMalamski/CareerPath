package com.careerpath.infrastructure.persistence.jpa.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileExperienceEmbeddable {
    private String id;
    private String company;
    private String title;
    private String employmentType;
    private String location;
    private String startDate;
    private String endDate;
    private boolean current;
    private String description;
}

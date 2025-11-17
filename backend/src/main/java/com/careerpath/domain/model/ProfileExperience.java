package com.careerpath.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileExperience {
    private Long id;
    private String company;
    private String title;
    private String employmentType;
    private String location;
    private String startDate;
    private String endDate;
    private boolean isCurrent;
    private String description;
}

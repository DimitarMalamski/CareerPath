package com.careerpath.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileSkill {
    private String id;
    private String name;
    private String level;
}

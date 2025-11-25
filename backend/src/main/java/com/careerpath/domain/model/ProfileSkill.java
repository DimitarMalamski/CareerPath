package com.careerpath.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileSkill {
    private Long id;
    private String name;
    private String level;
}

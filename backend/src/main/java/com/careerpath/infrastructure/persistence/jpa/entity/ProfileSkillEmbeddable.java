package com.careerpath.infrastructure.persistence.jpa.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileSkillEmbeddable {
    private Long id;
    private String name;
    private String level;
}

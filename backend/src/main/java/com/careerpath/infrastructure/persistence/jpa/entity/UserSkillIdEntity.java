package com.careerpath.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class UserSkillIdEntity implements Serializable {
    private UUID userId;
    private Integer skillId;
}
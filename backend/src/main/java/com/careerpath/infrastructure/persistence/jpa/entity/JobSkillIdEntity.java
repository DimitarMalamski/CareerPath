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
public class JobSkillIdEntity implements Serializable {
    private UUID jobId;
    private Integer skillId;
}

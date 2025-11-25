package com.careerpath.domain.modelOld.ids;

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
public class JobSkillId implements Serializable {
    private UUID jobId;
    private Integer skillId;
}

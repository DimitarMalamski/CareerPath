package com.careerpath.model.ids;

import lombok.*;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
@Embeddable
public class ExperienceSkillId implements Serializable {
    private UUID experienceId;
    private Integer skillId;
}

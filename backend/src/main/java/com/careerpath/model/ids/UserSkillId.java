package com.careerpath.model.ids;

import lombok.*;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
@Embeddable
public class UserSkillId implements Serializable {
    private UUID userId;
    private Integer skillId;
}
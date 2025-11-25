package com.careerpath.infrastructure.persistence.jpa.entity;

import com.careerpath.domain.model.enums.SkillLevel;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_skills")
public class UserSkillEntity {

    @EmbeddedId
    private UserSkillIdEntity id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SkillLevel level;
}

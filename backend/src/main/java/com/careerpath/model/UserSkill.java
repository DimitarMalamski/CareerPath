package com.careerpath.model;

import com.careerpath.model.enums.SkillLevel;
import com.careerpath.model.ids.UserSkillId;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "user_skills")
public class UserSkill {
    @EmbeddedId private UserSkillId id;

    @ManyToOne @MapsId("userId") @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne @MapsId("skillId") @JoinColumn(name = "skill_id")
    private Skill skill;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private SkillLevel level;
}

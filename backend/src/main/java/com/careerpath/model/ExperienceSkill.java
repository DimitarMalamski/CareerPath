package com.careerpath.model;

import com.careerpath.model.ids.ExperienceSkillId;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "experience_skills")
public class ExperienceSkill {
    @EmbeddedId private ExperienceSkillId id;

    @ManyToOne @MapsId("experienceId") @JoinColumn(name = "experience_id")
    private Experience experience;

    @ManyToOne @MapsId("skillId") @JoinColumn(name = "skill_id")
    private Skill skill;
}

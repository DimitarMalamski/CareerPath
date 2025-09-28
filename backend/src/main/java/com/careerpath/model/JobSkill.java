package com.careerpath.model;

import com.careerpath.model.ids.JobSkillId;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "job_skills")
public class JobSkill {
    @EmbeddedId private JobSkillId id;

    @ManyToOne @MapsId("jobId") @JoinColumn(name = "job_id")
    private JobListing job;

    @ManyToOne @MapsId("skillId") @JoinColumn(name = "skill_id")
    private Skill skill;
}

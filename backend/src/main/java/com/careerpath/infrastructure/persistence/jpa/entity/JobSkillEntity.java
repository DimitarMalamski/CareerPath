package com.careerpath.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "job_skills")
public class JobSkillEntity {

    @EmbeddedId
    private JobSkillIdEntity id;

    @ManyToOne
    @MapsId("jobId")
    @JoinColumn(name = "job_id")
    private JobListingEntity job;

    @ManyToOne
    @MapsId("skillId")
    @JoinColumn(name = "skill_id")
    private SkillEntity skill;
}

package com.careerpath.repository;

import com.careerpath.model.ExperienceSkill;
import com.careerpath.model.ids.ExperienceSkillId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExperienceSkillRepository extends JpaRepository<ExperienceSkill, ExperienceSkillId> {}

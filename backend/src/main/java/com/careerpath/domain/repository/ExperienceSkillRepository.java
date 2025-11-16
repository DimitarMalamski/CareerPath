package com.careerpath.domain.repository;

import com.careerpath.domain.modelOld.ExperienceSkill;
import com.careerpath.domain.modelOld.ids.ExperienceSkillId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExperienceSkillRepository extends JpaRepository<ExperienceSkill, ExperienceSkillId> {}

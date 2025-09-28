package com.careerpath.repository;

import com.careerpath.model.JobSkill;
import com.careerpath.model.ids.JobSkillId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobSkillRepository extends JpaRepository<JobSkill, JobSkillId> {}

package com.careerpath.domain.repository;

import com.careerpath.domain.modelOld.ids.JobSkillId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobSkillRepository extends JpaRepository<JobSkill, JobSkillId> {
    List<JobSkill> findByJob(JobListing job);
}

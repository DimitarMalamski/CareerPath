package com.careerpath.repository;

import com.careerpath.model.JobListing;
import com.careerpath.model.JobSkill;
import com.careerpath.model.ids.JobSkillId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobSkillRepository extends JpaRepository<JobSkill, JobSkillId> {
    List<JobSkill> findByJob(JobListing job);
}

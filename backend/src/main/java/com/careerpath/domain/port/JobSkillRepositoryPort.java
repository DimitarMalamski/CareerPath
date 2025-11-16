package com.careerpath.domain.port;

import com.careerpath.domain.model.JobSkill;
import com.careerpath.domain.model.JobListing;

import java.util.List;

public interface JobSkillRepositoryPort {

    List<JobSkill> findByJobId(JobListing job);
}

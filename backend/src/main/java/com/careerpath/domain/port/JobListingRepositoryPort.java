package com.careerpath.domain.port;

import com.careerpath.domain.model.JobListing;
import com.careerpath.domain.model.Skill;
import com.careerpath.domain.model.enums.JobType;

import java.util.List;
import java.util.UUID;

public interface JobListingRepositoryPort {

    List<JobListing> findAll();

    JobListing findById(UUID id);

    List<JobListing> findRelatedJobs(UUID jobId, int limit);

    JobListing save(
            String title,
            String company,
            String location,
            JobType type,
            List<Skill> skills,
            String description
    );
}

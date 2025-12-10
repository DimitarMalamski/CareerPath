package com.careerpath.domain.port;

import com.careerpath.domain.model.JobListing;
import java.util.List;
import java.util.UUID;

public interface JobListingRepositoryPort {

    List<JobListing> findAll();

    JobListing findById(UUID id);

    List<JobListing> findRelatedJobs(UUID jobId, int limit);
}

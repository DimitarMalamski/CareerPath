package com.careerpath.domain.port;

import com.careerpath.domain.model.JobListing;
import com.careerpath.domain.model.JobMatchResult;
import com.careerpath.domain.model.Profile;

import java.util.List;

public interface AiJobMatcherPort {
    List<JobMatchResult> matchJobs(Profile profile, List<JobListing> jobListings);
}

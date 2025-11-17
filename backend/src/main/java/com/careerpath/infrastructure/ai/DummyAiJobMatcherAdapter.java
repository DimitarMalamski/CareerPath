package com.careerpath.infrastructure.ai;

import com.careerpath.domain.model.JobListing;
import com.careerpath.domain.model.Profile;
import com.careerpath.domain.model.JobMatchResult;
import com.careerpath.domain.port.AiJobMatcherPort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DummyAiJobMatcherAdapter implements AiJobMatcherPort {

    @Override
    public List<JobMatchResult> matchJobs(Profile profile, List<JobListing> jobs) {
        return jobs.stream()
                .map(job -> JobMatchResult.builder()
                        .jobListingId(job.getId().toString())
                        .score(0.5)
                        .explanation("Dummy AI match")
                        .build()
                )
                .toList();
    }
}

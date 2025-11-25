package com.careerpath.domain.port;

import com.careerpath.domain.model.JobMatchResult;
import com.careerpath.domain.model.Profile;

import java.util.List;

public interface AiJobMatcherPort {
    List<JobMatchResult> enhanceMatches(Profile profile, List<JobMatchResult> baselineResults);
}

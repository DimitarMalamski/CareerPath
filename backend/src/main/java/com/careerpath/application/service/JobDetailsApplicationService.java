package com.careerpath.application.service;

import com.careerpath.application.dto.JobDetailsDto;
import com.careerpath.application.mapper.JobDetailsMapper;
import com.careerpath.domain.model.JobListing;
import com.careerpath.domain.model.JobMatchResult;
import com.careerpath.domain.model.Profile;
import com.careerpath.domain.port.AiJobMatcherPort;
import com.careerpath.domain.port.JobListingRepositoryPort;
import com.careerpath.domain.port.ProfilePersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobDetailsApplicationService {

    private final ProfilePersistencePort profilePersistencePort;
    private final JobListingRepositoryPort jobListingRepositoryPort;
    private final JobScoringService jobScoringService;
    private final AiJobMatcherPort aiJobMatcherPort;

    public JobDetailsDto getJobDetails(UUID jobId, String userIdFromJwt) {
        Profile profile = profilePersistencePort.findByUserId(userIdFromJwt)
                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + userIdFromJwt));

        JobListing jobListing = jobListingRepositoryPort.findById(jobId);

        JobMatchResult baseline = jobScoringService.score(profile, List.of(jobListing)).get(0);

        JobMatchResult enhanced = aiJobMatcherPort.enhanceMatches(profile, List.of(baseline)).get(0);

        return JobDetailsMapper.toDto(jobListing, enhanced);
    }

}

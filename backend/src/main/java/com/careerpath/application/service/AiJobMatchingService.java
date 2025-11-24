package com.careerpath.application.service;

import com.careerpath.application.dto.JobRecommendationDto;
import com.careerpath.application.mapper.JobRecommendationMapper;
import com.careerpath.domain.model.*;
import com.careerpath.domain.port.AiJobMatcherPort;
import com.careerpath.domain.port.JobListingRepositoryPort;
import com.careerpath.domain.port.ProfileRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AiJobMatchingService {
    private final ProfileRepositoryPort profileRepository;
    private final JobListingRepositoryPort jobListingRepository;
    private final JobScoringService scoringService;
    private final AiJobMatcherPort aiJobMatcherPort;
    private final JobRecommendationMapper jobRecommendationMapper;

    public List<JobRecommendationDto> getRecommendations(UUID userId) {
        Profile profile = profileRepository.getProfileByUserId(userId);

        List<JobListing> listings = jobListingRepository.findAll();

        List<JobMatchResult> baselineResults = scoringService.score(profile, listings);

        List<JobMatchResult> enhancedResults = aiJobMatcherPort.enhanceMatches(profile, baselineResults);

        return enhancedResults.stream()
                .sorted(Comparator.comparingDouble(JobMatchResult::getFinalScore).reversed())
                .map(result -> {
                    JobListing job = jobListingRepository.findById(UUID.fromString(result.getJobListingId()));
                    return jobRecommendationMapper.toDto(result, job);
                })
                .toList();
    }

}


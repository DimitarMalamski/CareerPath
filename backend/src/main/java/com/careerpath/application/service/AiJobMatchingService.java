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
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
//@RequiredArgsConstructor
public class AiJobMatchingService {
//    private final ProfileRepositoryPort profileRepository;
//    private final JobListingRepositoryPort jobListingRepository;
//    private final JobScoringService scoringService;
//    private final AiJobMatcherPort aiJobMatcherPort;
//    private final JobRecommendationMapper jobRecommendationMapper;

    private final ProfileRepositoryPort profileRepository;
    private final JobListingRepositoryPort jobListingRepository;
    private final JobScoringService scoringService;
    private final AiJobMatcherPort aiJobMatcherPort;
    private final JobRecommendationMapper jobRecommendationMapper;

    public AiJobMatchingService(
            ProfileRepositoryPort profileRepository,
            JobListingRepositoryPort jobListingRepository,
            JobScoringService scoringService,
            AiJobMatcherPort aiJobMatcherPort,
            JobRecommendationMapper jobRecommendationMapper
    ) {
        this.profileRepository = profileRepository;
        this.jobListingRepository = jobListingRepository;
        this.scoringService = scoringService;
        this.aiJobMatcherPort = aiJobMatcherPort;
        this.jobRecommendationMapper = jobRecommendationMapper;

        // Debug statements:
        System.out.println("DEBUG: profileRepository = " + profileRepository);
        System.out.println("DEBUG: jobListingRepository = " + jobListingRepository);
        System.out.println("DEBUG: scoringService = " + scoringService);
        System.out.println("DEBUG: aiJobMatcherPort = " + aiJobMatcherPort);
        System.out.println("DEBUG: jobRecommendationMapper = " + jobRecommendationMapper);
    }

    // Caching
    private final Map<UUID, List<JobRecommendationDto>> cache = new ConcurrentHashMap<>();

    public List<JobRecommendationDto> getRecommendations(UUID userId) {
        if (cache.containsKey(userId)) return cache.get(userId);

        Profile profile = profileRepository.getProfileByUserId(userId);

        List<JobListing> listings = jobListingRepository.findAll();

        List<JobMatchResult> baselineResults = scoringService.score(profile, listings);

        List<JobMatchResult> enhancedResults = aiJobMatcherPort.enhanceMatches(profile, baselineResults);

        List<JobRecommendationDto> jobRecommendationDtos = enhancedResults.stream()
                .sorted(Comparator.comparingDouble(JobMatchResult::getFinalScore).reversed())
                .map(result -> {
                    JobListing job = jobListingRepository.findById(UUID.fromString(result.getJobListingId()));
                    return jobRecommendationMapper.toDto(result, job);
                })
                .toList();

        cache.put(userId, jobRecommendationDtos);

        return jobRecommendationDtos;
    }
}


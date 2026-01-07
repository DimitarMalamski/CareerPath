package com.careerpath.application.service;

import com.careerpath.application.dto.JobRecommendationDto;
import com.careerpath.application.mapper.JobRecommendationMapper;
import com.careerpath.domain.model.*;
import com.careerpath.domain.port.AiJobMatcherPort;
import com.careerpath.domain.port.JobListingRepositoryPort;
import com.careerpath.domain.port.ProfilePersistencePort;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AiJobMatchingService {
    private final ProfilePersistencePort profileRepository;
    private final JobListingRepositoryPort jobListingRepository;
    private final JobScoringService scoringService;
    private final AiJobMatcherPort aiJobMatcherPort;
    private final JobRecommendationMapper jobRecommendationMapper;

    // Caching
    private final Map<String, List<JobRecommendationDto>> cache = new ConcurrentHashMap<>();

    @PostConstruct
    void debugInjectedPort() {
        System.out.println(
                ">>> Injected AiJobMatcherPort = " +
                        aiJobMatcherPort.getClass().getName()
        );
    }

    public List<JobRecommendationDto> getRecommendations(String userId) {
        System.out.println(">>> getRecommendations CALLED for user " + userId);
        System.out.println(">>> cache hit: " + cache.containsKey(userId));

        // if (cache.containsKey(userId)) return cache.get(userId);

        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile missing for userId: " + userId));

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


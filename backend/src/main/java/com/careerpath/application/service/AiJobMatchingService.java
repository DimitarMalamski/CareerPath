package com.careerpath.application.service;

import com.careerpath.application.dto.JobRecommendationDto;
import com.careerpath.application.mapper.JobRecommendationMapper;
import com.careerpath.domain.model.*;
import com.careerpath.domain.port.AiJobMatcherPort;
import com.careerpath.domain.port.JobListingRepositoryPort;
import com.careerpath.domain.port.ProfilePersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiJobMatchingService {

    private final ProfilePersistencePort profileRepository;
    private final JobListingRepositoryPort jobListingRepository;
    private final JobScoringService scoringService;
    private final AiJobMatcherPort aiJobMatcherPort;
    private final JobRecommendationMapper jobRecommendationMapper;

    private final Map<String, List<JobRecommendationDto>> cache = new ConcurrentHashMap<>();

    public List<JobRecommendationDto> getRecommendations(String userId) {
        log.info("Generating job recommendations for user {}", userId);

        if (cache.containsKey(userId)) return cache.get(userId);

        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException(
                        "Profile missing for userId: " + userId
                ));

        List<JobListing> listings = jobListingRepository.findAll();

        List<JobMatchResult> baselineResults =
                scoringService.score(profile, listings);

        List<JobMatchResult> enhancedResults =
                aiJobMatcherPort.enhanceMatches(profile, baselineResults);

        List<JobRecommendationDto> recommendations = enhancedResults.stream()
                .sorted(Comparator.comparingDouble(JobMatchResult::getFinalScore).reversed())
                .map(result -> {
                    JobListing job = jobListingRepository.findById(
                            UUID.fromString(result.getJobListingId())
                    );
                    return jobRecommendationMapper.toDto(result, job);
                })
                .toList();

        cache.put(userId, recommendations);
        return recommendations;
    }

    public void invalidateCache(String userId) {
        cache.remove(userId);
    }
}

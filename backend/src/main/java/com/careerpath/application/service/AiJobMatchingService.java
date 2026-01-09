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

    private static final int AI_LIMIT = 3;

    private final Map<String, List<JobRecommendationDto>> cache = new ConcurrentHashMap<>();

    public List<JobRecommendationDto> getRecommendations(String userId) {
        log.info("Generating job recommendations for user {}", userId);

        if (cache.containsKey(userId)) {
            log.info("Returning cached job recommendations for user {}", userId);
            return cache.get(userId);
        }

        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException(
                        "Profile missing for userId: " + userId
                ));

        List<JobListing> listings = jobListingRepository.findAll();

        List<JobMatchResult> baselineResults =
                scoringService.score(profile, listings);

        baselineResults.forEach(r -> r.setFinalScore(r.getScore()));

        List<JobMatchResult> sortedBaseline =
                baselineResults.stream()
                        .sorted(Comparator.comparingDouble(JobMatchResult::getScore).reversed())
                        .toList();

        List<JobMatchResult> aiCandidates =
                sortedBaseline.stream()
                        .limit(AI_LIMIT)
                        .toList();

        aiJobMatcherPort.enhanceMatches(profile, aiCandidates);

        List<JobRecommendationDto> recommendations =
                sortedBaseline.stream()
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

    public void invalidateAll() {
        log.info("Invalidating ALL job recommendation caches");
        cache.clear();
    }
}

package com.careerpath.application.service;

import com.careerpath.application.dto.JobRecommendationDto;
import com.careerpath.application.mapper.JobRecommendationMapper;
import com.careerpath.domain.model.*;
import com.careerpath.domain.port.AiJobMatcherPort;
import com.careerpath.domain.port.JobListingRepositoryPort;
import com.careerpath.domain.port.ProfilePersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AiJobMatchingServiceTest {

    private final ProfilePersistencePort profileRepository = mock(ProfilePersistencePort.class);
    private final JobListingRepositoryPort jobListingRepository = mock(JobListingRepositoryPort.class);
    private final JobScoringService scoringService = mock(JobScoringService.class);
    private final AiJobMatcherPort aiJobMatcherPort = mock(AiJobMatcherPort.class);
    private final JobRecommendationMapper jobRecommendationMapper = mock(JobRecommendationMapper.class);

    private AiJobMatchingService service;

    private String userId;
    private Profile profile;
    private JobListing job;
    private JobMatchResult baseline;
    private JobMatchResult enhanced;

    @BeforeEach
    void setup() {
        service = new AiJobMatchingService(
                profileRepository,
                jobListingRepository,
                scoringService,
                aiJobMatcherPort,
                jobRecommendationMapper
        );

        userId = "test-user-id-123";

        profile = Profile.builder()
                .userId(userId)
                .skills(List.of(
                        ProfileSkill.builder()
                                .id(1L)
                                .name("Java")
                                .level("EXPERT")
                                .build()
                ))
                .build();

        job = JobListing.builder()
                .id(UUID.randomUUID())
                .title("Backend Developer")
                .company("Google")
                .skills(List.of(Skill.builder().id(1).name("Java").build()))
                .createdAt(OffsetDateTime.now())
                .build();

        baseline = JobMatchResult.builder()
                .jobListingId(job.getId().toString())
                .score(0.7)
                .finalScore(0.7)
                .matchedSkills(List.of("Java"))
                .missingSkills(List.of())
                .build();

        enhanced = JobMatchResult.builder()
                .jobListingId(job.getId().toString())
                .score(0.7)
                .finalScore(0.9)
                .aiExplanation("Good match due to Java")
                .matchedSkills(List.of("Java"))
                .missingSkills(List.of())
                .build();
    }

    @Test
    void getRecommendations_shouldReturnEnhancedAndSortedResults() {
        when(profileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));
        when(jobListingRepository.findAll()).thenReturn(List.of(job));
        when(scoringService.score(profile, List.of(job))).thenReturn(List.of(baseline));
        when(aiJobMatcherPort.enhanceMatches(profile, List.of(baseline))).thenReturn(List.of(enhanced));
        when(jobListingRepository.findById(job.getId())).thenReturn(job);

        JobRecommendationDto dto = new JobRecommendationDto(
                job.getId().toString(),
                job.getTitle(),
                job.getCompany(),
                "loc",
                "type",
                List.of("Java"),
                "desc",
                enhanced.getFinalScore(),
                enhanced.getAiExplanation(),
                job.getCreatedAt().toString(),
                enhanced.getMatchedSkills(),
                enhanced.getMissingSkills()
        );

        when(jobRecommendationMapper.toDto(enhanced, job)).thenReturn(dto);

        List<JobRecommendationDto> result = service.getRecommendations(userId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).finalScore()).isEqualTo(0.9);
    }

    @Test
    void getRecommendations_shouldCacheResults() {
        when(profileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));
        when(jobListingRepository.findAll()).thenReturn(List.of(job));
        when(scoringService.score(profile, List.of(job))).thenReturn(List.of(baseline));
        when(aiJobMatcherPort.enhanceMatches(profile, List.of(baseline))).thenReturn(List.of(enhanced));
        when(jobListingRepository.findById(job.getId())).thenReturn(job);

        JobRecommendationDto dto = mock(JobRecommendationDto.class);
        when(jobRecommendationMapper.toDto(any(), any())).thenReturn(dto);

        service.getRecommendations(userId);
        service.getRecommendations(userId);

        verify(profileRepository, times(1)).findByUserId(userId);
        verify(jobListingRepository, times(1)).findAll();
        verify(scoringService, times(1)).score(any(), any());
        verify(aiJobMatcherPort, times(1)).enhanceMatches(any(), any());
    }

    @Test
    void getRecommendations_shouldReturnEmptyList_whenNoJobs() {
        when(profileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));
        when(jobListingRepository.findAll()).thenReturn(List.of());

        List<JobRecommendationDto> result = service.getRecommendations(userId);

        assertThat(result).isEmpty();
    }
}

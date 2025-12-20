package com.careerpath.application.service;

import com.careerpath.application.dto.JobDetailsDto;
import com.careerpath.application.dto.JobListingDto;
import com.careerpath.domain.model.*;
import com.careerpath.domain.model.enums.JobType;
import com.careerpath.domain.port.AiJobMatcherPort;
import com.careerpath.domain.port.JobListingRepositoryPort;
import com.careerpath.domain.port.ProfilePersistencePort;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobDetailsApplicationServiceTest {

    @Mock
    private ProfilePersistencePort profilePersistencePort;

    @Mock
    private JobListingRepositoryPort jobListingRepositoryPort;

    @Mock
    private JobScoringService jobScoringService;

    @Mock
    private AiJobMatcherPort aiJobMatcherPort;

    @InjectMocks
    private JobDetailsApplicationService service;

    @Test
    void getJobDetails_shouldReturnMappedDto() {
        // -------- Arrange --------
        UUID jobId = UUID.randomUUID();
        String userId = "user-123";

        Profile profile = Profile.builder()
                .userId(userId)
                .fullName("John Doe")
                .aiOptIn(true)
                .build();

        JobListing job = JobListing.builder()
                .id(jobId)
                .title("Backend Engineer")
                .company("Acme Corp")
                .location("Berlin")
                .type(JobType.FULL_TIME)
                .description("Job description")
                .stackSummary("Java, Spring Boot")
                .skills(List.of(
                        Skill.builder().id(1).name("Java").build(),
                        Skill.builder().id(2).name("Spring Boot").build()
                ))
                .build();

        JobMatchResult baseline = JobMatchResult.builder()
                .jobListingId(jobId.toString())
                .score(0.65)
                .matchedSkills(List.of("Java"))
                .missingSkills(List.of("Spring Boot"))
                .build();

        JobMatchResult enhanced = JobMatchResult.builder()
                .jobListingId(jobId.toString())
                .finalScore(0.82)
                .aiExplanation("Strong match due to Java expertise.")
                .matchedSkills(List.of("Java"))
                .missingSkills(List.of("Spring Boot"))
                .build();

        when(profilePersistencePort.findByUserId(userId))
                .thenReturn(Optional.of(profile));

        when(jobListingRepositoryPort.findById(jobId))
                .thenReturn(job);

        when(jobScoringService.score(profile, List.of(job)))
                .thenReturn(List.of(baseline));

        when(aiJobMatcherPort.enhanceMatches(profile, List.of(baseline)))
                .thenReturn(List.of(enhanced));

        // -------- Act --------
        JobDetailsDto result = service.getJobDetails(jobId, userId);

        // -------- Assert --------
        assertThat(result.id()).isEqualTo(jobId.toString());
        assertThat(result.title()).isEqualTo("Backend Engineer");
        assertThat(result.company()).isEqualTo("Acme Corp");
        assertThat(result.finalScore()).isEqualTo(0.82);
        assertThat(result.aiExplanation()).contains("Strong match");
        assertThat(result.matchedSkills()).contains("Java");
        assertThat(result.missingSkills()).contains("Spring Boot");

        verify(profilePersistencePort).findByUserId(userId);
        verify(jobListingRepositoryPort).findById(jobId);
        verify(jobScoringService).score(profile, List.of(job));
        verify(aiJobMatcherPort).enhanceMatches(profile, List.of(baseline));
    }

    @Test
    void getJobDetails_shouldThrow_whenProfileDoesNotExist() {
        UUID jobId = UUID.randomUUID();
        String userId = "missing-user";

        when(profilePersistencePort.findByUserId(userId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getJobDetails(jobId, userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Profile not found");

        verify(profilePersistencePort).findByUserId(userId);
        verifyNoMoreInteractions(jobListingRepositoryPort, jobScoringService, aiJobMatcherPort);
    }

    @Test
    void getRelatedJobs_shouldReturnMappedJobListingDtos() {
        // -------- Arrange --------
        UUID jobId = UUID.randomUUID();

        JobListing job1 = JobListing.builder()
                .id(UUID.randomUUID())
                .title("Backend Engineer")
                .company("Acme")
                .location("Berlin")
                .type(JobType.FULL_TIME)
                .skills(List.of())
                .build();

        JobListing job2 = JobListing.builder()
                .id(UUID.randomUUID())
                .title("Frontend Engineer")
                .company("Globex")
                .location("Amsterdam")
                .type(JobType.FULL_TIME)
                .skills(List.of())
                .build();

        when(jobListingRepositoryPort.findRelatedJobs(jobId, 3))
                .thenReturn(List.of(job1, job2));

        List<JobListingDto> result = service.getRelatedJobs(jobId);

        assertThat(result).hasSize(2);

        assertThat(result.get(0).title()).isEqualTo("Backend Engineer");
        assertThat(result.get(0).company()).isEqualTo("Acme");

        assertThat(result.get(1).title()).isEqualTo("Frontend Engineer");
        assertThat(result.get(1).company()).isEqualTo("Globex");

        verify(jobListingRepositoryPort).findRelatedJobs(jobId, 3);
        verifyNoMoreInteractions(jobListingRepositoryPort);
    }
}

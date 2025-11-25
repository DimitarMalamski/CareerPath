package com.careerpath.application.mapper;

import com.careerpath.application.dto.JobRecommendationDto;
import com.careerpath.domain.model.*;
import com.careerpath.domain.model.enums.JobType;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JobRecommendationMapperTest {

    private final JobRecommendationMapper mapper = new JobRecommendationMapper();

    @Test
    void toDto_shouldMapAllFieldsCorrectly() {
        // Arrange
        UUID jobId = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();

        JobListing job = JobListing.builder()
                .id(jobId)
                .title("Backend Developer")
                .company("Google")
                .location("Eindhoven")
                .type(JobType.FULL_TIME)
                .skills(List.of(
                        Skill.builder().id(1).name("Java").build(),
                        Skill.builder().id(2).name("Spring Boot").build()
                ))
                .description("Build backend APIs")
                .createdAt(now)
                .build();

        JobMatchResult match = JobMatchResult.builder()
                .jobListingId(jobId.toString())
                .finalScore(0.88)
                .aiExplanation("Strong Java match")
                .matchedSkills(List.of("Java"))
                .missingSkills(List.of("Spring Boot"))
                .build();

        // Act
        JobRecommendationDto dto = mapper.toDto(match, job);

        // Assert
        assertThat(dto.id()).isEqualTo(jobId.toString());
        assertThat(dto.title()).isEqualTo("Backend Developer");
        assertThat(dto.company()).isEqualTo("Google");
        assertThat(dto.location()).isEqualTo("Eindhoven");
        assertThat(dto.type()).isEqualTo("FULL_TIME");

        assertThat(dto.skills()).containsExactly("Java", "Spring Boot");
        assertThat(dto.description()).isEqualTo("Build backend APIs");

        assertThat(dto.finalScore()).isEqualTo(0.88);
        assertThat(dto.aiExplanation()).isEqualTo("Strong Java match");

        assertThat(dto.createdAt()).isEqualTo(now.toString());
        assertThat(dto.matchedSkills()).containsExactly("Java");
        assertThat(dto.missingSkills()).containsExactly("Spring Boot");
    }

    @Test
    void toDto_shouldHandleEmptySkillLists() {
        // Arrange
        JobListing job = JobListing.builder()
                .id(UUID.randomUUID())
                .title("No Skills Job")
                .company("TestCo")
                .location("Remote")
                .type(JobType.INTERNSHIP)
                .skills(List.of())
                .description("Entry-level role")
                .createdAt(OffsetDateTime.now())
                .build();

        JobMatchResult match = JobMatchResult.builder()
                .jobListingId(job.getId().toString())
                .finalScore(0.2)
                .aiExplanation(null)
                .matchedSkills(List.of())
                .missingSkills(List.of())
                .build();

        // Act
        JobRecommendationDto dto = mapper.toDto(match, job);

        // Assert
        assertThat(dto.skills()).isEmpty();
        assertThat(dto.matchedSkills()).isEmpty();
        assertThat(dto.missingSkills()).isEmpty();
        assertThat(dto.aiExplanation()).isNull();
    }
}

package com.careerpath.application.mapper;

import com.careerpath.application.dto.JobDetailsDto;
import com.careerpath.domain.model.JobListing;
import com.careerpath.domain.model.JobMatchResult;
import com.careerpath.domain.model.Skill;
import com.careerpath.domain.model.enums.JobStatus;
import com.careerpath.domain.model.enums.JobType;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class JobDetailsMapperTest {

    @Test
    void constructor_shouldThrowUnsupportedOperationException() throws Exception {
        Constructor<JobDetailsMapper> constructor =
                JobDetailsMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        assertThatThrownBy(constructor::newInstance)
                .hasCauseInstanceOf(UnsupportedOperationException.class)
                .hasRootCauseMessage("Utility class");
    }

    @Test
    void toDto_shouldMapAllFieldsCorrectly() {
        // Arrange
        UUID jobId = UUID.randomUUID();

        JobListing job = JobListing.builder()
                .id(jobId)
                .title("Senior Backend Developer")
                .company("Google")
                .location("Berlin")
                .type(JobType.FULL_TIME)
                .description("Build APIs")
                .stackSummary("Java, Spring Boot")
                .status(JobStatus.PUBLISHED)
                .expiresAt(LocalDate.parse("2025-12-01"))
                .createdAt(OffsetDateTime.parse("2025-01-01T10:00:00Z"))
                .updatedAt(OffsetDateTime.parse("2025-01-02T10:00:00Z"))
                .skills(List.of(
                        Skill.builder().id(1).name("Java").build(),
                        Skill.builder().id(2).name("Spring Boot").build()
                ))
                .build();

        JobMatchResult match = JobMatchResult.builder()
                .jobListingId(jobId.toString())
                .finalScore(87.5)
                .aiExplanation("Strong backend experience")
                .matchedSkills(List.of("Java"))
                .missingSkills(List.of("Docker"))
                .build();

        // Act
        JobDetailsDto dto = JobDetailsMapper.toDto(job, match);

        // Assert
        assertThat(dto.id()).isEqualTo(jobId.toString());
        assertThat(dto.title()).isEqualTo("Senior Backend Developer");
        assertThat(dto.company()).isEqualTo("Google");
        assertThat(dto.location()).isEqualTo("Berlin");
        assertThat(dto.type()).isEqualTo("FULL_TIME");
        assertThat(dto.description()).isEqualTo("Build APIs");
        assertThat(dto.stackSummary()).isEqualTo("Java, Spring Boot");

        assertThat(dto.finalScore()).isEqualTo(87.5);
        assertThat(dto.aiExplanation()).isEqualTo("Strong backend experience");

        assertThat(dto.createdAt()).isEqualTo("2025-01-01T10:00Z");
        assertThat(dto.updatedAt()).isEqualTo("2025-01-02T10:00Z");
        assertThat(dto.expiresAt()).isEqualTo("2025-12-01");

        assertThat(dto.skills()).containsExactly("Java", "Spring Boot");
        assertThat(dto.matchedSkills()).containsExactly("Java");
        assertThat(dto.missingSkills()).containsExactly("Docker");

        assertThat(dto.applyUrl()).isNull();
    }

    @Test
    void toDto_shouldHandleNullDatesAndSkills() {
        // Arrange
        JobListing job = JobListing.builder()
                .id(UUID.randomUUID())
                .title("No Dates Job")
                .company("TestCorp")
                .type(JobType.REMOTE)
                .skills(null)
                .build();

        JobMatchResult match = JobMatchResult.builder()
                .finalScore(50)
                .aiExplanation("Test explanation")
                .matchedSkills(List.of())
                .missingSkills(List.of())
                .build();

        // Act
        JobDetailsDto dto = JobDetailsMapper.toDto(job, match);

        // Assert
        assertThat(dto.createdAt()).isNull();
        assertThat(dto.updatedAt()).isNull();
        assertThat(dto.expiresAt()).isNull();
        assertThat(dto.skills()).isNull();
    }

    @Test
    void toDto_shouldExtractSkillNamesCorrectly() {
        JobListing job = JobListing.builder()
                .id(UUID.randomUUID())
                .title("Example Title")
                .company("Example Corp")
                .location("Remote")
                .type(JobType.FULL_TIME)
                .description("Test")
                .stackSummary("React, TypeScript")
                .skills(List.of(
                        Skill.builder().id(1).name("React").build(),
                        Skill.builder().id(2).name("TypeScript").build()
                ))
                .build();

        JobMatchResult match = JobMatchResult.builder()
                .finalScore(80)
                .aiExplanation("Test")
                .matchedSkills(List.of("React"))
                .missingSkills(List.of("GraphQL"))
                .build();

        JobDetailsDto dto = JobDetailsMapper.toDto(job, match);

        assertThat(dto.skills()).containsExactly("React", "TypeScript");
    }
}

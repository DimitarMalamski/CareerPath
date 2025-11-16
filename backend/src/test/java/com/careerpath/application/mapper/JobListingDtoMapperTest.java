package com.careerpath.application.mapper;

import com.careerpath.application.dto.JobListingDto;
import com.careerpath.domain.model.JobListing;
import com.careerpath.domain.model.Skill;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JobListingDtoMapperTest {

    @Test
    void toDto_shouldMapAllFieldsCorrectly() {
        JobListing jobListing = JobListing.builder()
                .id(UUID.randomUUID())
                .title("Backend Developer")
                .company("Google")
                .location("Amsterdam")
                .stackSummary("Java")
                .skills(List.of(
                        Skill.builder().id(1).name("Java").build()
                ))
                .build();

        JobListingDto dto = JobListingDtoMapper.toDto(jobListing);

        assertThat(dto.title()).isEqualTo("Backend Developer");
        assertThat(dto.skills()).containsExactly("Java");
    }

    @Test
    void toDto_shouldHandleEmptySkills() {
        JobListing jobListing = JobListing.builder()
                .title("No skill job listing")
                .skills(List.of())
                .build();

        JobListingDto dto = JobListingDtoMapper.toDto(jobListing);

        assertThat(dto.skills()).isEmpty();
    }
}

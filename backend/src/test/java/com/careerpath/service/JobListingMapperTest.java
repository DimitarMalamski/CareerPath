package com.careerpath.service;

import com.careerpath.dto.JobListingDto;
import com.careerpath.mapper.JobListingMapper;
import com.careerpath.model.JobListing;
import com.careerpath.model.JobSkill;
import com.careerpath.model.Skill;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JobListingMapperTest {
    @Test
    void toDto_shouldMapAllFieldsCorrectly() {
        // Arrange
        JobListing jobListing = JobListing.builder()
                .id(UUID.randomUUID())
                .title("Backend Developer")
                .company("Google")
                .location("Amsterdam")
                .stackSummary("Java")
                .build();

        Skill skill = new Skill();
        skill.setName("Java");
        JobSkill jobSkill = new JobSkill();
        jobSkill.setSkill(skill);

        // Act
        JobListingDto jobListingDto = JobListingMapper.toDto(jobListing, List.of(jobSkill));

        // Assert
        assertThat(jobListingDto.title()).isEqualTo("Backend Developer");
        assertThat(jobListingDto.skills()).containsExactly("Java");
    }

    @Test
    void toDto_shouldHandleEmptySkills() {
        // Arrange
        JobListing jobListing = JobListing.builder().title("No skill job listing").build();

        // Act
        JobListingDto jobListingDto = JobListingMapper.toDto(jobListing, List.of());

        // Assert
        assertThat(jobListingDto.skills()).isEmpty();
    }
}

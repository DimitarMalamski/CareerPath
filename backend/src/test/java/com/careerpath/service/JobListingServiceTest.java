package com.careerpath.service;

import com.careerpath.dto.JobListingDto;
import com.careerpath.model.JobListing;
import com.careerpath.model.JobSkill;
import com.careerpath.model.Skill;
import com.careerpath.model.enums.JobStatus;
import com.careerpath.model.enums.JobType;
import com.careerpath.repository.JobListingRepository;
import com.careerpath.repository.JobSkillRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JobListingServiceTest {

    @Mock
    private JobListingRepository jobListingRepository;

    @Mock
    private JobSkillRepository jobSkillRepository;

    @InjectMocks
    private JobListingService jobListingService;

    @Test
    void getAll_shouldReturnMappedJobListings() {
        // Arrange
        JobListing jobListing = JobListing.builder()
                .id(UUID.randomUUID())
                .title("Backend Developer")
                .company("Google")
                .location("Berlin, Germany")
                .type(JobType.FULL_TIME)
                .status(JobStatus.PUBLISHED)
                .stackSummary("Java, Spring Boot")
                .expiresAt(LocalDate.now().plusMonths(3))
                .build();

        Skill skill = new Skill();
        skill.setName("Java");
        JobSkill jobSkill = new JobSkill();
        jobSkill.setSkill(skill);

        when(jobListingRepository.findAll())
                .thenReturn(List.of(jobListing));
        when(jobSkillRepository.findByJob(jobListing))
                .thenReturn(List.of(jobSkill));

        // Act
        List<JobListingDto> result = jobListingService.getAll();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).title()).isEqualTo("Backend Developer");
        assertThat(result.get(0).skills()).contains("Java");

        // Verify
        verify(jobListingRepository, times(1)).findAll();
        verify(jobSkillRepository, times(1)).findByJob(jobListing);
    }

    @Test
    void getAll_shouldReturnEmptyList_whenNoJobsExist() {
        // Arrange
        when(jobListingRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<JobListingDto> result = jobListingService.getAll();

        // Assert
        assertThat(result).isEmpty();

        // Verify
        verify(jobListingRepository, times(1)).findAll();
        verify(jobSkillRepository, never()).findByJob(any());
    }

    @Test
    void getAll_shouldHandleJobsWithoutSkills() {
        // Arrange
        JobListing jobListing = JobListing.builder()
                .id(UUID.randomUUID())
                .title("Frontend Developer")
                .company("Google")
                .type(JobType.INTERNSHIP)
                .status(JobStatus.PUBLISHED)
                .build();

        when(jobListingRepository.findAll()).thenReturn(List.of(jobListing));
        when(jobSkillRepository.findByJob(jobListing)).thenReturn(Collections.emptyList());

        // Act
        List<JobListingDto> result = jobListingService.getAll();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).skills()).isEmpty();
    }

    @Test
    void getAll_shouldPropagateException_ifRepositoryFails() {
        // Arrange
        when(jobListingRepository.findAll())
                .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThatThrownBy(() -> jobListingService.getAll())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Database error");

        // Verify
        verify(jobListingRepository, times(1)).findAll();
    }
}

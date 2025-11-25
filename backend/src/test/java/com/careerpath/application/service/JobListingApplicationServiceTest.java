package com.careerpath.application.service;

import com.careerpath.application.dto.JobListingDto;
import com.careerpath.domain.model.JobListing;
import com.careerpath.domain.model.Skill;
import com.careerpath.domain.port.JobListingRepositoryPort;
import com.careerpath.domain.model.enums.JobStatus;
import com.careerpath.domain.model.enums.JobType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobListingApplicationServiceTest {

    @Mock
    private JobListingRepositoryPort jobListingRepository;

    @InjectMocks
    private JobListingApplicationService jobListingService;

    @Test
    void getAllJobListings_shouldReturnMappedDtos() {
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
                .skills(List.of(
                        Skill.builder().id(1).name("Java").build()
                ))
                .build();

        when(jobListingRepository.findAll()).thenReturn(List.of(jobListing));

        // Act
        List<JobListingDto> result = jobListingService.getAllJobListings();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).title()).isEqualTo("Backend Developer");
        assertThat(result.get(0).skills()).contains("Java");

        verify(jobListingRepository, times(1)).findAll();
    }

    @Test
    void getAllJobListings_shouldReturnEmptyList_whenNoJobsExist() {
        when(jobListingRepository.findAll()).thenReturn(Collections.emptyList());

        List<JobListingDto> result = jobListingService.getAllJobListings();

        assertThat(result).isEmpty();
        verify(jobListingRepository).findAll();
    }

    @Test
    void getAllJobListings_shouldHandleJobsWithoutSkills() {
        JobListing jobListing = JobListing.builder()
                .id(UUID.randomUUID())
                .title("Frontend Developer")
                .company("Google")
                .type(JobType.INTERNSHIP)
                .status(JobStatus.PUBLISHED)
                .skills(Collections.emptyList())
                .build();

        when(jobListingRepository.findAll()).thenReturn(List.of(jobListing));

        List<JobListingDto> result = jobListingService.getAllJobListings();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).skills()).isEmpty();
    }

    @Test
    void getAllJobListings_shouldPropagateException_ifRepositoryFails() {
        when(jobListingRepository.findAll())
                .thenThrow(new RuntimeException("Database error"));

        assertThatThrownBy(() -> jobListingService.getAllJobListings())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Database error");

        verify(jobListingRepository).findAll();
    }
}

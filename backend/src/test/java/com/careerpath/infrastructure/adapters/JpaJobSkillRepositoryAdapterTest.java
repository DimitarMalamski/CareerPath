package com.careerpath.infrastructure.adapters;

import com.careerpath.domain.model.JobListing;
import com.careerpath.domain.model.JobSkill;
import com.careerpath.infrastructure.persistence.jpa.adapter.JpaJobSkillRepositoryAdapter;
import com.careerpath.infrastructure.persistence.jpa.entity.*;
import com.careerpath.infrastructure.persistence.jpa.repository.SpringDataJobSkillRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaJobSkillRepositoryAdapterTest {

    @Mock
    private SpringDataJobSkillRepository jobSkillRepository;

    @InjectMocks
    private JpaJobSkillRepositoryAdapter adapter;

    @Test
    void findByJobId_shouldReturnMappedDomainObjects() {
        // Arrange
        UUID jobId = UUID.randomUUID();

        JobListing job = JobListing.builder()
                .id(jobId)
                .build();

        SkillEntity skillEntity = SkillEntity.builder()
                .id(5)
                .name("Java")
                .build();

        JobListingEntity jobEntity = JobListingEntity.builder()
                .id(jobId)
                .build();

        JobSkillIdEntity key = new JobSkillIdEntity(jobId, 5);

        JobSkillEntity jobSkillEntity = JobSkillEntity.builder()
                .id(key)
                .job(jobEntity)
                .skill(skillEntity)
                .build();

        when(jobSkillRepository.findByJob(any(JobListingEntity.class)))
                .thenReturn(List.of(jobSkillEntity));

        // Act
        List<JobSkill> result = adapter.findByJobId(job);

        // Assert
        assertThat(result).hasSize(1);

        JobSkill jobSkill = result.get(0);

        assertThat(jobSkill.getJobId()).isEqualTo(jobId);
        assertThat(jobSkill.getSkillId()).isEqualTo(5);

        ArgumentCaptor<JobListingEntity> captor = ArgumentCaptor.forClass(JobListingEntity.class);
        verify(jobSkillRepository).findByJob(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(jobId);
    }

    @Test
    void findByJobId_shouldReturnEmptyList_whenNoSkillsFound() {
        // Arrange
        UUID jobId = UUID.randomUUID();

        JobListing job = JobListing.builder()
                .id(jobId)
                .build();

        when(jobSkillRepository.findByJob(any(JobListingEntity.class)))
                .thenReturn(List.of());

        // Act
        List<JobSkill> result = adapter.findByJobId(job);

        // Assert
        assertThat(result).isEmpty();
        verify(jobSkillRepository).findByJob(any(JobListingEntity.class));
    }
}

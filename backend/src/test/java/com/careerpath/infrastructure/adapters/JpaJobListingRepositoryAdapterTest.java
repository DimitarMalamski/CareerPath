package com.careerpath.infrastructure.adapters;

import com.careerpath.domain.model.JobListing;
import com.careerpath.domain.model.Skill;
import com.careerpath.domain.model.enums.JobType;
import com.careerpath.infrastructure.persistence.jpa.adapter.JpaJobListingRepositoryAdapter;
import com.careerpath.infrastructure.persistence.jpa.entity.JobListingEntity;
import com.careerpath.infrastructure.persistence.jpa.entity.JobSkillEntity;
import com.careerpath.infrastructure.persistence.jpa.entity.JobSkillIdEntity;
import com.careerpath.infrastructure.persistence.jpa.entity.SkillEntity;
import com.careerpath.infrastructure.persistence.jpa.repository.SpringDataJobListingRepository;
import com.careerpath.infrastructure.persistence.jpa.repository.SpringDataJobSkillRepository;

import com.careerpath.infrastructure.persistence.jpa.repository.SpringDataSkillRepository;
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
class JpaJobListingRepositoryAdapterTest {

    @Mock
    private SpringDataJobListingRepository jobListingRepository;

    @Mock
    private SpringDataJobSkillRepository jobSkillRepository;

    @Mock
    private SpringDataSkillRepository skillRepository;

    @InjectMocks
    private JpaJobListingRepositoryAdapter adapter;

    @Test
    void findAll_shouldReturnMappedDomainObjectsWithSkills() {
        // Arrange
        UUID jobId = UUID.randomUUID();

        JobListingEntity jobEntity = JobListingEntity.builder()
                .id(jobId)
                .title("Backend Dev")
                .company("Google")
                .location("Amsterdam")
                .description("Backend role")
                .build();

        SkillEntity skillEntity = SkillEntity.builder()
                .id(1)
                .name("Java")
                .build();

        JobSkillEntity jobSkill = JobSkillEntity.builder()
                .id(new JobSkillIdEntity(jobEntity.getId(), skillEntity.getId()))
                .job(jobEntity)
                .skill(skillEntity)
                .build();

        when(jobListingRepository.findAll()).thenReturn(List.of(jobEntity));
        when(jobSkillRepository.findByJob(jobEntity)).thenReturn(List.of(jobSkill));

        // Act
        List<JobListing> results = adapter.findAll();

        // Assert
        assertThat(results).hasSize(1);
        JobListing listing = results.get(0);

        assertThat(listing.getId()).isEqualTo(jobId);
        assertThat(listing.getTitle()).isEqualTo("Backend Dev");
        assertThat(listing.getSkills())
                .extracting(Skill::getName)
                .containsExactly("Java");

        verify(jobListingRepository).findAll();
        verify(jobSkillRepository).findByJob(jobEntity);
    }

    @Test
    void findById_shouldReturnMappedDomainWithSkills() {
        // Arrange
        UUID jobId = UUID.randomUUID();

        JobListingEntity jobEntity = JobListingEntity.builder()
                .id(jobId)
                .title("Backend Dev")
                .company("Meta")
                .build();

        SkillEntity skillEntity = SkillEntity.builder()
                .id(2)
                .name("Spring Boot")
                .build();

        JobSkillEntity jobSkill = JobSkillEntity.builder()
                .job(jobEntity)
                .skill(skillEntity)
                .build();

        when(jobListingRepository.findById(jobId)).thenReturn(Optional.of(jobEntity));
        when(jobSkillRepository.findByJob(jobEntity)).thenReturn(List.of(jobSkill));

        // Act
        JobListing result = adapter.findById(jobId);

        // Assert
        assertThat(result.getCompany()).isEqualTo("Meta");
        assertThat(result.getSkills())
                .extracting(Skill::getName)
                .containsExactly("Spring Boot");

        verify(jobListingRepository).findById(jobId);
        verify(jobSkillRepository).findByJob(jobEntity);
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        UUID jobId = UUID.randomUUID();

        when(jobListingRepository.findById(jobId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adapter.findById(jobId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Job listing now found");

        verify(jobListingRepository).findById(jobId);
        verify(jobSkillRepository, never()).findByJob(any());
    }

    @Test
    void findRelatedJobs_shouldReturnJobsInCorrectOrder() {
        UUID jobId = UUID.randomUUID();

        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID id3 = UUID.randomUUID();

        List<UUID> relatedIds = List.of(id2, id1, id3);

        JobListingEntity e1 = JobListingEntity.builder().id(id1).title("First").build();
        JobListingEntity e2 = JobListingEntity.builder().id(id2).title("Second").build();
        JobListingEntity e3 = JobListingEntity.builder().id(id3).title("Third").build();

        when(jobListingRepository.findRelatedJobIds(eq(jobId), any()))
                .thenReturn(relatedIds);

        when(jobListingRepository.findAllById(relatedIds))
                .thenReturn(List.of(e1, e2, e3));

        when(jobSkillRepository.findByJob(any()))
                .thenReturn(List.of());

        // Act
        List<JobListing> results = adapter.findRelatedJobs(jobId, 3);

        assertThat(results)
                .extracting(JobListing::getId)
                .containsExactly(id2, id1, id3);

        verify(jobListingRepository).findRelatedJobIds(eq(jobId), any());
        verify(jobListingRepository).findAllById(relatedIds);
    }

    @Test
    void findRelatedJobs_shouldSkipMissingEntities() {
        UUID jobId = UUID.randomUUID();

        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        when(jobListingRepository.findRelatedJobIds(eq(jobId), any()))
                .thenReturn(List.of(id1, id2));

        JobListingEntity e1 = JobListingEntity.builder().id(id1).title("Only exists").build();

        when(jobListingRepository.findAllById(List.of(id1, id2)))
                .thenReturn(List.of(e1));

        when(jobSkillRepository.findByJob(e1)).thenReturn(List.of());

        List<JobListing> results = adapter.findRelatedJobs(jobId, 2);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getId()).isEqualTo(id1);
    }

    @Test
    void findRelatedJobs_shouldIncludeSkills() {
        UUID jobId = UUID.randomUUID();
        UUID relatedId = UUID.randomUUID();

        when(jobListingRepository.findRelatedJobIds(eq(jobId), any()))
                .thenReturn(List.of(relatedId));

        JobListingEntity jobEntity = JobListingEntity.builder()
                .id(relatedId)
                .title("DevOps Engineer")
                .build();

        SkillEntity skillEntity = SkillEntity.builder()
                .id(10)
                .name("Docker")
                .build();

        JobSkillEntity jobSkill = JobSkillEntity.builder()
                .job(jobEntity)
                .skill(skillEntity)
                .build();

        when(jobListingRepository.findAllById(List.of(relatedId)))
                .thenReturn(List.of(jobEntity));

        when(jobSkillRepository.findByJob(jobEntity))
                .thenReturn(List.of(jobSkill));

        List<JobListing> results = adapter.findRelatedJobs(jobId, 1);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getSkills())
                .extracting(Skill::getName)
                .containsExactly("Docker");
    }

    @Test
    void save_shouldPersistJobWithoutSkills() {
        JobListingEntity savedEntity = JobListingEntity.builder()
                .id(UUID.randomUUID())
                .title("Backend Dev")
                .company("Google")
                .build();

        when(jobListingRepository.save(any()))
                .thenReturn(savedEntity);

        when(jobSkillRepository.findByJob(savedEntity))
                .thenReturn(List.of());

        JobListing result = adapter.save(
                "Backend Dev",
                "Google",
                "Berlin",
                JobType.FULL_TIME,
                List.of(),
                "desc"
        );

        assertThat(result.getTitle()).isEqualTo("Backend Dev");
        assertThat(result.getSkills()).isEmpty();

        verify(jobListingRepository).save(any());
        verify(jobSkillRepository, never()).save(any());
    }

    @Test
    void save_shouldPersistJobWithSkills() {
        UUID jobId = UUID.randomUUID();

        JobListingEntity savedEntity = JobListingEntity.builder()
                .id(jobId)
                .title("Backend Dev")
                .company("Google")
                .build();

        Skill skill = Skill.builder()
                .name("Java")
                .build();

        SkillEntity skillEntity = SkillEntity.builder()
                .id(1)
                .name("Java")
                .build();

        when(jobListingRepository.save(any()))
                .thenReturn(savedEntity);

        when(skillRepository.findByName("Java"))
                .thenReturn(Optional.of(skillEntity));

        when(jobSkillRepository.findByJob(savedEntity))
                .thenReturn(List.of(
                        JobSkillEntity.builder()
                                .job(savedEntity)
                                .skill(skillEntity)
                                .build()
                ));

        JobListing result = adapter.save(
                "Backend Dev",
                "Google",
                "Berlin",
                JobType.FULL_TIME,
                List.of(skill),
                "desc"
        );

        assertThat(result.getSkills())
                .extracting(Skill::getName)
                .containsExactly("Java");

        verify(jobSkillRepository).save(any(JobSkillEntity.class));
    }

    @Test
    void save_shouldThrowException_whenSkillNotFound() {
        JobListingEntity savedEntity = JobListingEntity.builder()
                .id(UUID.randomUUID())
                .build();

        when(jobListingRepository.save(any()))
                .thenReturn(savedEntity);

        when(skillRepository.findByName("NonExisting"))
                .thenReturn(Optional.empty());

        Skill skill = Skill.builder()
                .name("NonExisting")
                .build();

        List<Skill> skills = List.of(skill);

        assertThatThrownBy(() -> adapter.save(
                "Backend Dev",
                "Google",
                "Berlin",
                JobType.FULL_TIME,
                skills,
                "desc"
        ))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Skill not found");

        verify(jobSkillRepository, never()).save(any());
    }
}

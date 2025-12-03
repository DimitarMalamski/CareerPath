package com.careerpath.infrastructure.mappers;

import com.careerpath.domain.model.JobListing;
import com.careerpath.domain.model.Skill;
import com.careerpath.domain.model.enums.JobStatus;
import com.careerpath.domain.model.enums.JobType;
import com.careerpath.infrastructure.persistence.jpa.entity.JobListingEntity;
import com.careerpath.infrastructure.persistence.jpa.entity.JobSkillEntity;
import com.careerpath.infrastructure.persistence.jpa.entity.JobSkillIdEntity;
import com.careerpath.infrastructure.persistence.jpa.entity.SkillEntity;

import com.careerpath.infrastructure.persistence.jpa.mapper.JobListingEntityMapper;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class JobListingEntityMapperTest {

    @Test
    void constructor_shouldThrowUnsupportedOperationException() throws Exception {
        var constructor = JobListingEntityMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        assertThatThrownBy(constructor::newInstance)
                .hasCauseInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void toDomain_shouldMapAllFieldsIncludingSkills() {
        // Arrange
        UUID jobId = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();

        // Skill entity
        SkillEntity skillEntity = SkillEntity.builder()
                .id(10)
                .name("Java")
                .build();

        // JobSkill entity (composite key)
        JobSkillEntity jobSkillEntity = JobSkillEntity.builder()
                .id(new JobSkillIdEntity(jobId, 10))
                .skill(skillEntity)
                .build();

        JobListingEntity entity = JobListingEntity.builder()
                .id(jobId)
                .recruiterId("recruiter-1")
                .title("Backend Developer")
                .company("Google")
                .location("Amsterdam")
                .type(JobType.FULL_TIME)
                .stackSummary("Java, Spring")
                .description("Build APIs")
                .status(JobStatus.PUBLISHED)
                .expiresAt(now.plusDays(30).toLocalDate())
                .createdAt(now)
                .updatedAt(now)
                .deletedAt(null)
                .jobSkills(List.of(jobSkillEntity))
                .build();

        // Act
        JobListing domain = JobListingEntityMapper.toDomain(entity);

        // Assert scalar fields
        assertThat(domain.getId()).isEqualTo(jobId);
        assertThat(domain.getTitle()).isEqualTo("Backend Developer");
        assertThat(domain.getCompany()).isEqualTo("Google");
        assertThat(domain.getLocation()).isEqualTo("Amsterdam");
        assertThat(domain.getRecruiterId()).isEqualTo("recruiter-1");
        assertThat(domain.getType()).isEqualTo(JobType.FULL_TIME);
        assertThat(domain.getStatus()).isEqualTo(JobStatus.PUBLISHED);
        assertThat(domain.getDescription()).isEqualTo("Build APIs");
        assertThat(domain.getCreatedAt()).isEqualTo(now);

        // Assert skills list
        assertThat(domain.getSkills())
                .hasSize(1)
                .extracting(Skill::getName, Skill::getId)
                .containsExactly(tuple("Java", 10));
    }

    @Test
    void toDomain_shouldReturnEmptySkillList_whenEntityHasNoSkills() {
        // Arrange
        JobListingEntity entity = JobListingEntity.builder()
                .id(UUID.randomUUID())
                .jobSkills(null)
                .build();

        // Act
        JobListing domain = JobListingEntityMapper.toDomain(entity);

        // Assert
        assertThat(domain.getSkills()).isEmpty();
    }
}

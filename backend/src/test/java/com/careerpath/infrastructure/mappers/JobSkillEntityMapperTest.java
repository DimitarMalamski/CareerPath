package com.careerpath.infrastructure.mappers;

import com.careerpath.domain.model.JobSkill;
import com.careerpath.infrastructure.persistence.jpa.entity.JobListingEntity;
import com.careerpath.infrastructure.persistence.jpa.entity.JobSkillEntity;
import com.careerpath.infrastructure.persistence.jpa.entity.JobSkillIdEntity;
import com.careerpath.infrastructure.persistence.jpa.entity.SkillEntity;

import com.careerpath.infrastructure.persistence.jpa.mapper.JobSkillEntityMapper;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class JobSkillEntityMapperTest {

    @Test
    void constructor_shouldThrowUnsupportedOperationException() throws Exception {
        var constructor = JobSkillEntityMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        assertThatThrownBy(constructor::newInstance)
                .hasCauseInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void toDomain_shouldMapJobSkillEntityToDomainCorrectly() {
        // Arrange
        UUID jobId = UUID.randomUUID();
        int skillId = 42;

        JobListingEntity jobEntity = JobListingEntity.builder()
                .id(jobId)
                .build();

        SkillEntity skillEntity = SkillEntity.builder()
                .id(skillId)
                .name("Java")
                .build();

        JobSkillEntity jobSkillEntity = JobSkillEntity.builder()
                .id(new JobSkillIdEntity(jobId, skillId))
                .job(jobEntity)
                .skill(skillEntity)
                .build();

        // Act
        JobSkill domain = JobSkillEntityMapper.toDomain(jobSkillEntity);

        // Assert
        assertThat(domain.getJobId()).isEqualTo(jobId);
        assertThat(domain.getSkillId()).isEqualTo(skillId);
    }
}

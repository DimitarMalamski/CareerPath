package com.careerpath.infrastructure.mappers;

import com.careerpath.domain.model.Skill;
import com.careerpath.infrastructure.persistence.jpa.entity.SkillEntity;
import com.careerpath.infrastructure.persistence.jpa.mapper.SkillEntityMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SkillEntityMapperTest {

    @Test
    void constructor_shouldThrowUnsupportedOperationException() throws Exception {
        var ctor = SkillEntityMapper.class.getDeclaredConstructor();
        ctor.setAccessible(true);

        assertThatThrownBy(ctor::newInstance)
                .hasCauseInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void toDomain_shouldMapEntityToDomainCorrectly() {
        // Arrange
        SkillEntity entity = SkillEntity.builder()
                .id(5)
                .name("Java")
                .build();

        // Act
        Skill domain = SkillEntityMapper.toDomain(entity);

        // Assert
        assertThat(domain.getId()).isEqualTo(5);
        assertThat(domain.getName()).isEqualTo("Java");
    }
}

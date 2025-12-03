package com.careerpath.application.mapper;

import com.careerpath.application.dto.*;
import com.careerpath.domain.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ProfileMapperTest {

    @Test
    void constructor_shouldThrowUnsupportedOperationException() throws Exception {
        var constructor = ProfileMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        assertThatThrownBy(constructor::newInstance)
                .hasCauseInstanceOf(UnsupportedOperationException.class)
                .hasRootCauseMessage("Utility class");
    }

    @Test
    void toDto_shouldMapProfileToDtoCorrectly() {
        // Arrange
        ProfileSkill skill = ProfileSkill.builder()
                .id("1")
                .name("Java")
                .level("advanced")
                .build();

        ProfileExperience experience = ProfileExperience.builder()
                .id("exp1")
                .company("Google")
                .title("Backend Developer")
                .employmentType("FULL_TIME")
                .location("Amsterdam")
                .startDate("2020-01-01")
                .endDate("2021-06-01")
                .isCurrent(false)
                .description("Built APIs")
                .build();

        Profile domain = Profile.builder()
                .userId("user123")
                .fullName("John Doe")
                .headline("Backend Engineer")
                .about("Passionate dev")
                .location("Netherlands")
                .skills(List.of(skill))
                .experiences(List.of(experience))
                .aiOptIn(true)
                .build();

        // Act
        ProfileDto dto = ProfileMapper.toDto(domain);

        // Assert main fields
        assertThat(dto.fullName()).isEqualTo("John Doe");
        assertThat(dto.headline()).isEqualTo("Backend Engineer");
        assertThat(dto.about()).isEqualTo("Passionate dev");
        assertThat(dto.location()).isEqualTo("Netherlands");
        assertThat(dto.aiOptIn()).isTrue();

        // Assert skills
        assertThat(dto.skills())
                .extracting(ProfileSkillDto::id, ProfileSkillDto::name, ProfileSkillDto::level)
                .containsExactly(tuple("1", "Java", "advanced"));

        // Assert experiences
        assertThat(dto.experiences())
                .extracting(
                        ProfileExperienceDto::id,
                        ProfileExperienceDto::company,
                        ProfileExperienceDto::title
                )
                .containsExactly(tuple("exp1", "Google", "Backend Developer"));
    }

    @Test
    void toDomain_shouldMapDtoToProfileCorrectly() {
        // Arrange
        ProfileSkillDto skillDto = new ProfileSkillDto(
                "1",
                "Java",
                "advanced"
        );

        ProfileExperienceDto expDto = new ProfileExperienceDto(
                "exp1",
                "Google",
                "Backend Developer",
                "FULL_TIME",
                "Amsterdam",
                "2020-01-01",
                "2021-06-01",
                false,
                "Built APIs"
        );

        ProfileDto dto = new ProfileDto(
                "John Doe",
                "Backend Engineer",
                "About text",
                "Netherlands",
                List.of(skillDto),
                List.of(expDto),
                true
        );

        String userId = "user123";

        // Act
        Profile domain = ProfileMapper.toDomain(userId, dto);

        // Assert main fields
        assertThat(domain.getUserId()).isEqualTo("user123");
        assertThat(domain.getFullName()).isEqualTo("John Doe");
        assertThat(domain.getHeadline()).isEqualTo("Backend Engineer");
        assertThat(domain.getAbout()).isEqualTo("About text");
        assertThat(domain.getLocation()).isEqualTo("Netherlands");
        assertThat(domain.isAiOptIn()).isTrue();

        // Assert skills
        assertThat(domain.getSkills())
                .extracting(ProfileSkill::getId, ProfileSkill::getName, ProfileSkill::getLevel)
                .containsExactly(tuple("1", "Java", "advanced"));

        // Assert experiences
        assertThat(domain.getExperiences())
                .extracting(
                        ProfileExperience::getId,
                        ProfileExperience::getCompany,
                        ProfileExperience::getTitle
                )
                .containsExactly(tuple("exp1", "Google", "Backend Developer"));
    }

    @Test
    void toDomainSkill_shouldGenerateId_whenDtoIdIsNull() {
        // Arrange
        ProfileSkillDto dto = new ProfileSkillDto(null, "Python", "beginner");

        // Act
        ProfileSkill domain = ProfileMapper.toDomain(dto);

        // Assert
        assertThat(domain.getId()).isNotNull();
        assertThat(domain.getName()).isEqualTo("Python");
        assertThat(domain.getLevel()).isEqualTo("beginner");
    }

    @Test
    void toDomainExperience_shouldGenerateId_whenDtoIdIsNull() {
        // Arrange
        ProfileExperienceDto dto = new ProfileExperienceDto(
                null,
                "Meta",
                "Engineer",
                "FULL_TIME",
                "Remote",
                "2020-01-01",
                null,
                true,
                "Worked on backend"
        );

        // Act
        ProfileExperience domain = ProfileMapper.toDomain(dto);

        // Assert
        assertThat(domain.getId()).isNotNull();
        assertThat(domain.getCompany()).isEqualTo("Meta");
        assertThat(domain.getTitle()).isEqualTo("Engineer");
        assertThat(domain.isCurrent()).isTrue();
    }
}

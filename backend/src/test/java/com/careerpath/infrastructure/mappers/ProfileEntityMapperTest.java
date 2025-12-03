package com.careerpath.infrastructure.mappers;

import com.careerpath.domain.model.*;
import com.careerpath.infrastructure.persistence.jpa.entity.*;
import com.careerpath.infrastructure.persistence.jpa.mapper.ProfileEntityMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ProfileEntityMapperTest {

    @Test
    void constructor_shouldThrowUnsupportedOperationException() throws Exception {
        var ctor = ProfileEntityMapper.class.getDeclaredConstructor();
        ctor.setAccessible(true);

        assertThatThrownBy(ctor::newInstance)
                .hasCauseInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void toDomain_shouldMapEntityToDomainCorrectly() {
        ProfileSkillEmbeddable skill1 = ProfileSkillEmbeddable.builder()
                .id("s1")
                .name("Java")
                .level("advanced")
                .build();

        ProfileExperienceEmbeddable exp1 = ProfileExperienceEmbeddable.builder()
                .id("e1")
                .company("Google")
                .title("Engineer")
                .employmentType("FULL_TIME")
                .location("Berlin")
                .startDate("2020-01-01")
                .endDate("2021-01-01")
                .current(false)
                .description("Worked on backend")
                .build();

        ProfileDataEmbeddable data = ProfileDataEmbeddable.builder()
                .fullName("John Doe")
                .headline("Backend Dev")
                .about("About me")
                .location("Netherlands")
                .skills(List.of(skill1))
                .experiences(List.of(exp1))
                .build();

        ProfileEntity entity = ProfileEntity.builder()
                .userId("user123")
                .data(data)
                .aiOptIn(true)
                .build();

        // Act
        Profile profile = ProfileEntityMapper.toDomain(entity);

        // Assert
        assertThat(profile.getUserId()).isEqualTo("user123");
        assertThat(profile.getFullName()).isEqualTo("John Doe");
        assertThat(profile.getHeadline()).isEqualTo("Backend Dev");
        assertThat(profile.getAbout()).isEqualTo("About me");
        assertThat(profile.getLocation()).isEqualTo("Netherlands");
        assertThat(profile.isAiOptIn()).isTrue();

        // Skills
        assertThat(profile.getSkills())
                .hasSize(1)
                .extracting(ProfileSkill::getId, ProfileSkill::getName, ProfileSkill::getLevel)
                .containsExactly(tuple("s1", "Java", "advanced"));

        // Experiences
        assertThat(profile.getExperiences())
                .hasSize(1)
                .extracting(ProfileExperience::getId, ProfileExperience::getCompany, ProfileExperience::getTitle)
                .containsExactly(tuple("e1", "Google", "Engineer"));
    }

    @Test
    void toDomain_shouldReturnEmptyListsWhenEmbeddablesAreNull() {
        ProfileDataEmbeddable data = ProfileDataEmbeddable.builder()
                .fullName("John")
                .headline("Dev")
                .about("About")
                .location("Earth")
                .skills(null)
                .experiences(null)
                .build();

        ProfileEntity entity = ProfileEntity.builder()
                .userId("u1")
                .data(data)
                .aiOptIn(false)
                .build();

        Profile profile = ProfileEntityMapper.toDomain(entity);

        assertThat(profile.getSkills()).isEmpty();
        assertThat(profile.getExperiences()).isEmpty();
    }

    @Test
    void toEntity_shouldMapDomainToEntityCorrectly() {
        ProfileSkill skill = ProfileSkill.builder()
                .id("1")
                .name("Java")
                .level("advanced")
                .build();

        ProfileExperience exp = ProfileExperience.builder()
                .id("e1")
                .company("Meta")
                .title("Engineer")
                .employmentType("FULL_TIME")
                .location("Remote")
                .startDate("2020-01-01")
                .endDate("2021-01-01")
                .isCurrent(false)
                .description("Did stuff")
                .build();

        Profile domain = Profile.builder()
                .userId("userXYZ")
                .fullName("Alice")
                .headline("Dev")
                .about("About her")
                .location("EU")
                .skills(List.of(skill))
                .experiences(List.of(exp))
                .aiOptIn(true)
                .build();

        // Act
        ProfileEntity entity = ProfileEntityMapper.toEntity(domain);

        // Assert
        assertThat(entity.getUserId()).isEqualTo("userXYZ");
        assertThat(entity.isAiOptIn()).isTrue();

        ProfileDataEmbeddable data = entity.getData();
        assertThat(data.getFullName()).isEqualTo("Alice");
        assertThat(data.getHeadline()).isEqualTo("Dev");
        assertThat(data.getAbout()).isEqualTo("About her");
        assertThat(data.getLocation()).isEqualTo("EU");

        // Skills
        assertThat(data.getSkills())
                .hasSize(1)
                .extracting(ProfileSkillEmbeddable::getId, ProfileSkillEmbeddable::getName)
                .containsExactly(tuple("1", "Java"));

        // Experiences
        assertThat(data.getExperiences())
                .hasSize(1)
                .extracting(ProfileExperienceEmbeddable::getId, ProfileExperienceEmbeddable::getCompany)
                .containsExactly(tuple("e1", "Meta"));
    }
}

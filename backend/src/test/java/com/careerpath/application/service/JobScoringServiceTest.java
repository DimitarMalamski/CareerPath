package com.careerpath.application.service;

import com.careerpath.domain.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JobScoringServiceTest {

    private final JobScoringService scoringService = new JobScoringService();

    private Profile createProfile() {
        Profile profile = new Profile();

        profile.setSkills(List.of(
                ProfileSkill.builder()
                        .id("1L")
                        .name("Java")
                        .level("EXPERT")
                        .build()
        ));

        profile.setExperiences(List.of(
                ProfileExperience.builder()
                        .title("Backend Developer")
                        .description("Worked with Java and Spring Boot")
                        .build()
        ));

        profile.setLocation("Eindhoven");

        return profile;
    }

    private JobListing createJob(String title, String location, List<Skill> skills, String description) {
        return JobListing.builder()
                .id(UUID.randomUUID())
                .title(title)
                .company("TestCompany")
                .location(location)
                .skills(skills)
                .stackSummary(description)
                .description(description)
                .build();
    }

    @Test
    void scoreSingle_shouldScoreHigh_whenSkillsAndLocationMatch() {
        Profile profile = createProfile();

        JobListing job = createJob(
                "Java Backend Developer",
                "Eindhoven",
                List.of(
                        Skill.builder().id(1).name("Java").build(),
                        Skill.builder().id(2).name("Spring Boot").build()
                ),
                "Java Spring Boot Backend"
        );

        // Act
        JobMatchResult result = scoringService.scoreSingle(profile, job);

        // Assert
        assertThat(result.getScore()).isGreaterThan(0.5);
        assertThat(result.getMatchedSkills()).containsExactly("Java");
        assertThat(result.getMissingSkills()).contains("Spring Boot");
        assertThat(result.getExplanation()).contains("Matched skill");
    }

    @Test
    void scoreSingle_shouldScoreLow_whenNoSkillsMatch() {
        // Arrange
        Profile profile = createProfile();

        JobListing job = createJob(
                "Frontend Developer",
                "Berlin",
                List.of(Skill.builder().id(1).name("React").build()),
                "React Frontend"
        );

        // Act
        JobMatchResult result = scoringService.scoreSingle(profile, job);

        // Assert
        assertThat(result.getMatchedSkills()).isEmpty();
        assertThat(result.getMissingSkills()).contains("React");
        assertThat(result.getScore()).isLessThan(0.2);
    }

    @Test
    void scoreSingle_shouldGiveLocationBonus_whenLocationsMatch() {
        // Arrange
        Profile profile = createProfile();

        JobListing job = createJob(
                "Backend Developer",
                "Eindhoven",
                List.of(Skill.builder().id(1).name("Python").build()),
                "Python API Developer"
        );

        // Act
        JobMatchResult result = scoringService.scoreSingle(profile, job);

        // Assert
        assertThat(result.getScore()).isGreaterThan(0.05);
        assertThat(result.getExplanation()).contains("Location matches");
    }

    @Test
    void scoreSingle_shouldHandleJobWithNoSkills() {
        // Arrange
        Profile profile = createProfile();

        JobListing job = createJob(
                "Random Job",
                "Eindhoven",
                List.of(),
                "General role"
        );

        // Act
        JobMatchResult result = scoringService.scoreSingle(profile, job);

        // Assert
        assertThat(result.getMatchedSkills()).isEmpty();
        assertThat(result.getMissingSkills()).isEmpty();
        assertThat(result.getScore()).isGreaterThanOrEqualTo(0.05);
    }
}

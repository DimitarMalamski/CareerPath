package com.careerpath.integration.job;

import com.careerpath.BaseIntegrationTest;
import com.careerpath.model.JobListing;
import com.careerpath.model.User;
import com.careerpath.model.enums.JobStatus;
import com.careerpath.model.enums.JobType;
import com.careerpath.model.enums.UserRole;
import com.careerpath.repository.JobListingRepository;
import com.careerpath.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class JobRepositoryIntegrationTest extends BaseIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("careerpath_test")
            .withUsername("cp_user")
            .withPassword("cp_pass");

    @Autowired
    private JobListingRepository jobListingRepository;

    @Autowired
    private UserRepository userRepository;

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void saveAndRetrieveJobListing_shouldWorkWithRealDatabase() {
        System.out.println("Connected DB: " + postgres.getJdbcUrl());
        System.out.println("DB Username: " + postgres.getUsername());
        System.out.println("DB Password: " + postgres.getPassword());
        // Arrange
        User recruiter = User.builder()
                .email("recruiter@example.com")
                .passwordHash("hashedPassword123")
                .role(UserRole.RECRUITER)
                .build();

        recruiter = userRepository.save(recruiter);

        JobListing jobListing = JobListing.builder()
                .title("Backend Developer")
                .company("Google")
                .location("Berlin")
                .type(JobType.FULL_TIME)
                .status(JobStatus.PUBLISHED)
                .stackSummary("Spring Boot, PostgreSQL")
                .expiresAt(LocalDate.now().plusDays(30))
                .recruiter(recruiter)
                .description("Build and maintain APIs")
                .build();

        // Act
        jobListingRepository.save(jobListing);
        List<JobListing> jobListings = jobListingRepository.findAll();

        // Assert
        assertThat(jobListings).isNotEmpty();
        assertThat(jobListings.get(0).getCompany()).isEqualTo("Google");
        assertThat(jobListings.get(0).getRecruiter().getEmail()).isEqualTo("recruiter@example.com");
    }
}

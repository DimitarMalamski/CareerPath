package com.careerpath.infrastructure.config;

import com.careerpath.domain.model.enums.JobStatus;
import com.careerpath.domain.model.enums.JobType;
import com.careerpath.domain.model.enums.UserRole;
import com.careerpath.infrastructure.persistence.jpa.entity.JobListingEntity;
import com.careerpath.infrastructure.persistence.jpa.entity.UserEntity;
import com.careerpath.infrastructure.persistence.jpa.repository.SpringDataJobListingRepository;
import com.careerpath.infrastructure.persistence.jpa.repository.SpringDataUserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.util.List;

@Configuration
@Profile("prod")
public class ProdDatabaseSeeder {

    @Bean
    CommandLineRunner seedProdData(
            SpringDataUserRepository userRepository,
            SpringDataJobListingRepository jobRepository
    ) {
        return args -> {

            if (jobRepository.count() == 0) {

                // 1. Create recruiter user
                UserEntity recruiter = userRepository.save(
                        UserEntity.builder()
                                .email("recruiter@careerpath.com")
                                .passwordHash("hashedpassword456")
                                .role(UserRole.RECRUITER)
                                .build()
                );

                List<JobListingEntity> jobs = List.of(
                        JobListingEntity.builder()
                                .recruiterId(recruiter.getId())
                                .title("Full Stack Engineer")
                                .company("CareerPath Inc.")
                                .location("Amsterdam, NL")
                                .type(JobType.FULL_TIME)
                                .stackSummary("Angular, Spring Boot, PostgreSQL, Docker")
                                .description("Join the CareerPath core team and build scalable software for job seekers.")
                                .status(JobStatus.PUBLISHED)
                                .expiresAt(LocalDate.now().plusMonths(6))
                                .build(),

                        JobListingEntity.builder()
                                .recruiterId(recruiter.getId())
                                .title("Software Engineer Intern")
                                .company("CareerPath Labs")
                                .location("Eindhoven, NL")
                                .type(JobType.INTERNSHIP)
                                .stackSummary("Java, Angular, GitLab CI/CD")
                                .description("Work on innovative software tools and data-driven features for CareerPath.")
                                .status(JobStatus.PUBLISHED)
                                .expiresAt(LocalDate.now().plusMonths(2))
                                .build()
                );

                jobRepository.saveAll(jobs);
            }
        };
    }
}

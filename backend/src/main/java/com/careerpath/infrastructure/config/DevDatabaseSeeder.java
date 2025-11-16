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
@Profile("dev")
public class DevDatabaseSeeder {

    @Bean
    CommandLineRunner seedDevData(
            SpringDataUserRepository userRepository,
            SpringDataJobListingRepository jobListingRepository
    ) {
        return args -> {

            if (jobListingRepository.count() == 0) {

                UserEntity recruiter = userRepository.save(
                        UserEntity.builder()
                                .email("recruiter.dev@careerpath.com")
                                .passwordHash("hashedpassword123")
                                .role(UserRole.RECRUITER)
                                .build()
                );

                List<JobListingEntity> jobs = List.of(
                        JobListingEntity.builder()
                                .recruiterId(recruiter.getId())
                                .title("Frontend Developer (Angular)")
                                .company("SoftUni Tech")
                                .location("Eindhoven, NL")
                                .type(JobType.FULL_TIME)
                                .stackSummary("Angular, TypeScript, Tailwind")
                                .description("Work on modern, responsive UIs using Angular and Tailwind.")
                                .status(JobStatus.PUBLISHED)
                                .expiresAt(LocalDate.now().plusMonths(2))
                                .build(),

                        JobListingEntity.builder()
                                .recruiterId(recruiter.getId())
                                .title("Backend Developer (Spring Boot)")
                                .company("Fontys Digital Solutions")
                                .location("Tilburg, NL")
                                .type(JobType.FULL_TIME)
                                .stackSummary("Java, Spring Boot, PostgreSQL, Docker")
                                .description("Develop REST APIs and microservices for scalable web apps.")
                                .status(JobStatus.PUBLISHED)
                                .expiresAt(LocalDate.now().plusMonths(3))
                                .build(),

                        JobListingEntity.builder()
                                .recruiterId(recruiter.getId())
                                .title("AI Integration Intern")
                                .company("AI Dynamics")
                                .location("Remote")
                                .type(JobType.INTERNSHIP)
                                .stackSummary("Python, OpenAI API, Machine Learning basics")
                                .description("Assist in integrating AI models into SaaS products.")
                                .status(JobStatus.PENDING)
                                .expiresAt(LocalDate.now().plusWeeks(4))
                                .build()
                );

                jobListingRepository.saveAll(jobs);
            }
        };
    }
}

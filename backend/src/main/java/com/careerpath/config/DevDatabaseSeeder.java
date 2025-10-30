package com.careerpath.config;

import com.careerpath.model.JobListing;
import com.careerpath.model.User;
import com.careerpath.model.enums.JobStatus;
import com.careerpath.model.enums.JobType;
import com.careerpath.model.enums.UserRole;
import com.careerpath.repository.JobListingRepository;
import com.careerpath.repository.UserRepository;
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
    CommandLineRunner seedDevData(UserRepository userRepository, JobListingRepository jobListingRepository) {
        return args -> {
            if (jobListingRepository.count() == 0) {
                User recruiter = userRepository.save(
                        User.builder()
                                .email("recruiter.dev@careerpath.com")
                                .passwordHash("hashedpassword123")
                                .role(UserRole.RECRUITER)
                                .build()
                );

                List<JobListing> jobs = List.of(
                        JobListing.builder()
                                .recruiter(recruiter)
                                .title("Frontend Developer (Angular)")
                                .company("SoftUni Tech")
                                .location("Eindhoven, NL")
                                .type(JobType.FULL_TIME)
                                .stackSummary("Angular, TypeScript, Tailwind")
                                .description("Work on modern, responsive UIs using Angular and Tailwind.")
                                .status(JobStatus.PUBLISHED)
                                .expiresAt(LocalDate.now().plusMonths(2))
                                .build(),
                        JobListing.builder()
                                .recruiter(recruiter)
                                .title("Backend Developer (Spring Boot)")
                                .company("Fontys Digital Solutions")
                                .location("Tilburg, NL")
                                .type(JobType.FULL_TIME)
                                .stackSummary("Java, Spring Boot, PostgreSQL, Docker")
                                .description("Develop REST APIs and microservices for scalable web apps.")
                                .status(JobStatus.PUBLISHED)
                                .expiresAt(LocalDate.now().plusMonths(3))
                                .build(),
                        JobListing.builder()
                                .recruiter(recruiter)
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
                System.out.println("✅ Dev DB seeded with test data: " + jobs.size() + " job listings.");
            }
        };
    }
}

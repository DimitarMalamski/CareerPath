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
@Profile("prod")
public class ProdDatabaseSeeder {

    @Bean
    CommandLineRunner seedProdData(UserRepository userRepo, JobListingRepository jobRepo) {
        return args -> {
            if (jobRepo.count() == 0) {
                User recruiter = userRepo.save(
                        User.builder()
                                .email("recruiter@careerpath.com")
                                .passwordHash("hashedpassword456")
                                .role(UserRole.RECRUITER)
                                .build()
                );

                List<JobListing> jobs = List.of(
                        JobListing.builder()
                                .recruiter(recruiter)
                                .title("Full Stack Engineer")
                                .company("CareerPath Inc.")
                                .location("Amsterdam, NL")
                                .type(JobType.FULL_TIME)
                                .stackSummary("Angular, Spring Boot, PostgreSQL, Docker")
                                .description("Join the CareerPath core team and build scalable software for job seekers.")
                                .status(JobStatus.PUBLISHED)
                                .expiresAt(LocalDate.now().plusMonths(6))
                                .build(),
                        JobListing.builder()
                                .recruiter(recruiter)
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

                jobRepo.saveAll(jobs);
                System.out.println("✅ Prod DB seeded with demo data: " + jobs.size() + " job listings.");
            }
        };
    }
}

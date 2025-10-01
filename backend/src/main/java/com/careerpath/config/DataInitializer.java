package com.careerpath.config;

import com.careerpath.model.*;
import com.careerpath.model.enums.*;
import com.careerpath.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

// @Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {
    private final UserRepository users;
    private final ProfileRepository profiles;
    private final SkillRepository skills;
    private final JobListingRepository jobs;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        User recruiter  = users.findByEmail("recruiter@example.com")
                .orElseGet(() -> users.save(User.builder()
                        .email("recruiter@example.com")
                        .passwordHash("dev-only")
                        .role(UserRole.RECRUITER)
                        .build()));

        profiles.findById(recruiter.getId()).orElseGet(() ->
                profiles.save(Profile.builder()
                        .user(recruiter)
                        .firstName("Dimitar")
                        .aiOptIn(true)
                        .location("Eindhoven")
                        .headline("Hiring developer")
                        .build())
        );

        for (String name : List.of("Java", "C#", "Spring Boot", "Angular")) {
            skills.findByName(name).orElseGet(() -> skills.save(Skill.builder().name(name).build()));
        }

        if (jobs.count() == 0) {
            jobs.save(JobListing.builder()
                    .recruiter(recruiter)
                    .title("Junior Java Developer")
                    .company("Google")
                    .type(JobType.FULL_TIME)
                    .status(JobStatus.PUBLISHED)
                    .description("Build Web Applications using Java")
                    .stackSummary("Java, Spring Boot, PostgresSql")
                    .build());
        }
    }
}

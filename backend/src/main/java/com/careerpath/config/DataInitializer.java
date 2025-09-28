package com.careerpath.config;

import com.careerpath.model.*;
import com.careerpath.model.enums.*;
import com.careerpath.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {
    private final UserRepository users;
    private final ProfileRepository profiles;
    private final SkillRepository skills;
    private final JobListingRepository jobs;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        User user = users.findByEmail("recruiter@example.com")
                .orElseGet(() -> users.save(User.builder()
                        .email("recruiter@example.com")
                        .passwordHash("dev-only")
                        .role(UserRole.RECRUITER)
                        .build()));

        if (!profiles.existsById(user.getId())) {
            profiles.save(Profile.builder()
                    .user(user)
                    .firstName("Dimitar")
                    .lastName("Malamski")
                    .aiOptIn(true)
                    .build());
        }

        if (!skills.existsByName("Java")) {
            skills.save(Skill.builder().name("Java").build());
        }
        if (!skills.existsByName("C#")) {
            skills.save(Skill.builder().name("C#").build());
        }

        if (jobs.count() == 0) {
            jobs.save(JobListing.builder()
                    .recruiter(user)
                    .title("Junior Java Developer")
                    .company("Google")
                    .type(JobType.FULL_TIME)
                    .status(JobStatus.PUBLISHED)
                    .description("Build Web Applications using Java")
                    .build());
        }
    }
}

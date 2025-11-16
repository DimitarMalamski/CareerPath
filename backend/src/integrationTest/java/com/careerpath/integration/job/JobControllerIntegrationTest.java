package com.careerpath.integration.job;

import com.careerpath.BaseIntegrationTest;
import com.careerpath.domain.modelOld.User;
import com.careerpath.domain.modelOld.enums.JobStatus;
import com.careerpath.domain.modelOld.enums.JobType;
import com.careerpath.domain.modelOld.enums.UserRole;
import com.careerpath.domain.repository.JobListingRepository;
import com.careerpath.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
public class JobControllerIntegrationTest extends BaseIntegrationTest {

    private final MockMvc mockMvc;
    private final JobListingRepository jobListingRepository;
    private final UserRepository userRepository;

    @Autowired
    public JobControllerIntegrationTest(MockMvc mockMvc,
                                        JobListingRepository jobListingRepository,
                                        UserRepository userRepository) {
        this.mockMvc = mockMvc;
        this.jobListingRepository = jobListingRepository;
        this.userRepository = userRepository;
    }

    @BeforeEach
    void setupData() {
        jobListingRepository.deleteAll();
        userRepository.deleteAll();

        User recruiter = userRepository.save(
                User.builder()
                        .email("recruiter@example.com")
                        .passwordHash("hashedPassword123")
                        .role(UserRole.RECRUITER)
                        .build()
        );

        JobListing job = JobListing.builder()
                .recruiter(recruiter)
                .title("Backend Developer")
                .company("CareerPath Inc.")
                .location("Eindhoven, NL")
                .type(JobType.FULL_TIME)
                .stackSummary("Java, Spring Boot, PostgreSQL")
                .description("Build scalable backend services for our platform.")
                .status(JobStatus.PUBLISHED)
                .expiresAt(LocalDate.now().plusDays(30))
                .build();

        jobListingRepository.save(job);
    }

    @Test
    void shouldReturnAllJobListings() throws Exception {
        mockMvc.perform(get("/api/jobs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Backend Developer"))
                .andExpect(jsonPath("$[0].company").value("CareerPath Inc."))
                .andExpect(jsonPath("$[0].location").value("Eindhoven, NL"))
                .andExpect(jsonPath("$[0].type").value("FULL_TIME"))
                .andExpect(jsonPath("$[0].status").value("PUBLISHED"));
    }
}

package com.careerpath.integration.job;

import com.careerpath.BaseIntegrationTest;
import com.careerpath.domain.model.enums.JobStatus;
import com.careerpath.domain.model.enums.JobType;
import com.careerpath.domain.model.enums.UserRole;
import com.careerpath.domain.port.AiJobMatcherPort;
import com.careerpath.infrastructure.persistence.jpa.entity.JobListingEntity;
import com.careerpath.infrastructure.persistence.jpa.repository.SpringDataJobListingRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
public class JobControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpringDataJobListingRepository jobRepo;

    @MockitoBean
    private AiJobMatcherPort aiJobMatcherPort;

    @BeforeEach
    void setupData() {
        jobRepo.deleteAll();

        String recruiterId = "rest-recruiter-id-123";

        JobListingEntity job = JobListingEntity.builder()
                .recruiterId(recruiterId)
                .title("Backend Developer")
                .company("CareerPath Inc.")
                .location("Eindhoven, NL")
                .type(JobType.FULL_TIME)
                .stackSummary("Java, Spring Boot, PostgreSQL")
                .description("Build scalable backend services for our platform.")
                .status(JobStatus.PUBLISHED)
                .expiresAt(LocalDate.now().plusDays(30))
                .build();

        jobRepo.save(job);
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

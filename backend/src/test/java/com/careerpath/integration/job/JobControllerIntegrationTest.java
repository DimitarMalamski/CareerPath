package com.careerpath.integration.job;

import com.careerpath.controller.JobController;
import com.careerpath.dto.JobListingDto;
import com.careerpath.model.enums.JobStatus;
import com.careerpath.model.enums.JobType;
import com.careerpath.service.JobListingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@WebMvcTest(JobController.class)
class JobControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobListingService jobListingService;

    @Test
    void getAllJobListings_shouldReturnJsonArray() throws Exception {
        // Arrange
        JobListingDto jobListingDto = new JobListingDto(
                UUID.randomUUID(),
                "Backend Developer",
                "Google",
                "Berlin, Germany",
                "Java, Spring Boot",
                JobType.FULL_TIME,
                JobStatus.PUBLISHED,
                LocalDate.now().plusDays(30),
                List.of("Java", "Spring")
        );

        Mockito.when(jobListingService.getAll()).thenReturn(List.of(jobListingDto));

        // Act & Assert
        mockMvc.perform(get("/api/jobs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("Backend Developer")))
                .andExpect(jsonPath("$[0].company", is("Google")))
                .andExpect(jsonPath("$[0].skills[0]", is("Java")));
    }
}

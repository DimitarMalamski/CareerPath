package com.careerpath.interfaces.api;

import com.careerpath.application.dto.JobListingDto;
import com.careerpath.application.dto.JobRecommendationDto;
import com.careerpath.application.service.AiJobMatchingService;
import com.careerpath.application.service.JobListingApplicationService;
import com.careerpath.domain.model.enums.JobStatus;
import com.careerpath.domain.model.enums.JobType;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class JobControllerTest {

    @Test
    void getAllJobListings_shouldReturnListOfJobs() throws Exception {
        // Arrange
        JobListingApplicationService mockListingService = mock(JobListingApplicationService.class);
        AiJobMatchingService mockAiService = mock(AiJobMatchingService.class);

        JobListingDto jobListingDto = new JobListingDto(
                UUID.randomUUID(),
                "Frontend Developer",
                "Apple",
                "Amsterdam, NL",
                "Angular, Tailwind",
                JobType.FULL_TIME,
                JobStatus.PUBLISHED,
                LocalDate.now().plusMonths(2),
                List.of("Angular", "TypeScript")
        );

        when(mockListingService.getAllJobListings()).thenReturn(List.of(jobListingDto));

        JobController controller = new JobController(mockListingService, mockAiService);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        // Act & Assert
        mockMvc.perform(get("/api/jobs").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Frontend Developer"))
                .andExpect(jsonPath("$[0].skills[0]").value("Angular"));

        verify(mockListingService , times(1)).getAllJobListings();
    }

    @Test
    void getJobListingById_shouldReturnSingleJob() throws Exception {
        // Arrange
        JobListingApplicationService mockListingService = mock(JobListingApplicationService.class);
        AiJobMatchingService mockAiService = mock(AiJobMatchingService.class);

        UUID jobId = UUID.randomUUID();

        JobListingDto jobListingDto = new JobListingDto(
                jobId,
                "Backend Developer",
                "Google",
                "Eindhoven",
                "Java, Spring",
                JobType.FULL_TIME,
                JobStatus.PUBLISHED,
                LocalDate.now().plusMonths(3),
                List.of("Java", "Spring")
        );

        when(mockListingService.getJobListingById(jobId)).thenReturn(jobListingDto);

        JobController controller = new JobController(mockListingService, mockAiService);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        // Act & Assert
        mockMvc.perform(get("/api/jobs/" + jobId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Backend Developer"))
                .andExpect(jsonPath("$.company").value("Google"))
                .andExpect(jsonPath("$.skills[0]").value("Java"));

        verify(mockListingService, times(1)).getJobListingById(jobId);
    }

    @Test
    void recommendJobs_shouldReturnRecommendations() throws Exception {
        // Arrange
        JobListingApplicationService mockListingService = mock(JobListingApplicationService.class);
        AiJobMatchingService mockAiService = mock(AiJobMatchingService.class);

        String userId = "test-user-id-123";

        JobRecommendationDto recommendation = new JobRecommendationDto(
                UUID.randomUUID().toString(),
                "Java Developer",
                "Microsoft",
                "Amsterdam",
                JobType.FULL_TIME.name(),
                List.of("Java", "Spring Boot"),
                "Backend role",
                0.85,
                "Strong match with profile",
                LocalDate.now().toString(),
                List.of("Java"),
                List.of("Spring Boot")
        );

        when(mockAiService.getRecommendations(userId)).thenReturn(List.of(recommendation));

        JobController controller = new JobController(mockListingService, mockAiService);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        // Act & Assert
        mockMvc.perform(get("/api/jobs/recommendations/" + userId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Java Developer"))
                .andExpect(jsonPath("$[0].company").value("Microsoft"))
                .andExpect(jsonPath("$[0].finalScore").value(0.85));

        verify(mockAiService, times(1)).getRecommendations(userId);
    }
}

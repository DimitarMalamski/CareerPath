package com.careerpath.interfaces.api;

import com.careerpath.application.dto.JobDetailsDto;
import com.careerpath.application.dto.JobListingDto;
import com.careerpath.application.dto.JobRecommendationDto;
import com.careerpath.application.service.AiJobMatchingService;
import com.careerpath.application.service.JobDetailsApplicationService;
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
        JobDetailsApplicationService mockJobDetailsService = mock(JobDetailsApplicationService.class);

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

        JobController controller = new JobController(mockListingService, mockAiService, mockJobDetailsService);
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
        JobDetailsApplicationService mockJobDetailsService = mock(JobDetailsApplicationService.class);

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

        JobController controller = new JobController(mockListingService, mockAiService, mockJobDetailsService);
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
        JobDetailsApplicationService mockJobDetailsService = mock(JobDetailsApplicationService.class);

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

        JobController controller = new JobController(mockListingService, mockAiService, mockJobDetailsService);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        // Act & Assert
        mockMvc.perform(get("/api/jobs/recommendations/" + userId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Java Developer"))
                .andExpect(jsonPath("$[0].company").value("Microsoft"))
                .andExpect(jsonPath("$[0].finalScore").value(0.85));

        verify(mockAiService, times(1)).getRecommendations(userId);
    }

    @Test
    void getJobDetails_shouldReturnDetailsForUser() throws Exception {
        // Arrange
        JobListingApplicationService mockListingService = mock(JobListingApplicationService.class);
        AiJobMatchingService mockAiService = mock(AiJobMatchingService.class);
        JobDetailsApplicationService mockDetailsService = mock(JobDetailsApplicationService.class);

        UUID jobId = UUID.randomUUID();
        String userId = "test-user-123";

        JobDetailsDto detailsDto = new JobDetailsDto(
                jobId.toString(),
                "Senior Backend Engineer",
                "Google",
                "Berlin",
                "FULL_TIME",
                "Great job",
                "Java, Cloud",
                0.92,
                "Strong match",
                "2024-01-01",
                "2024-01-02",
                "2024-03-01",
                List.of("Java"),
                List.of("Java"),
                List.of("Docker"),
                null
        );

        when(mockDetailsService.getJobDetails(jobId, userId))
                .thenReturn(detailsDto);

        var authentication = mock(org.springframework.security.core.Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userId);

        var securityContext = mock(org.springframework.security.core.context.SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        org.springframework.security.core.context.SecurityContextHolder.setContext(securityContext);

        JobController controller = new JobController(mockListingService, mockAiService, mockDetailsService);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        // Act & Assert
        mockMvc.perform(get("/api/jobs/" + jobId + "/details")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Senior Backend Engineer"))
                .andExpect(jsonPath("$.company").value("Google"))
                .andExpect(jsonPath("$.finalScore").value(0.92));

        verify(mockDetailsService).getJobDetails(jobId, userId);
    }

    @Test
    void getRelatedJobs_shouldReturnRelatedListings() throws Exception {
        JobListingApplicationService mockListingService = mock(JobListingApplicationService.class);
        AiJobMatchingService mockAiService = mock(AiJobMatchingService.class);
        JobDetailsApplicationService mockDetailsService = mock(JobDetailsApplicationService.class);

        UUID jobId = UUID.randomUUID();

        JobListingDto related = new JobListingDto(
                jobId,
                "React Developer",
                "Netflix",
                "Remote",
                "React, TS",
                JobType.FULL_TIME,
                JobStatus.PUBLISHED,
                LocalDate.now().plusMonths(3),
                List.of("React")
        );

        when(mockDetailsService.getRelatedJobs(jobId))
                .thenReturn(List.of(related));

        JobController controller = new JobController(mockListingService, mockAiService, mockDetailsService);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        mockMvc.perform(get("/api/jobs/" + jobId + "/related")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("React Developer"))
                .andExpect(jsonPath("$[0].company").value("Netflix"));

        verify(mockDetailsService).getRelatedJobs(jobId);
    }
}

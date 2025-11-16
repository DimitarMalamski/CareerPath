package com.careerpath.interfaces.api;

import com.careerpath.application.dto.JobListingDto;
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
        JobListingApplicationService mockService = mock(JobListingApplicationService.class);

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

        when(mockService.getAllJobListings()).thenReturn(List.of(jobListingDto));

        JobController controller = new JobController(mockService);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        // Act & Assert
        mockMvc.perform(get("/api/jobs").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Frontend Developer"))
                .andExpect(jsonPath("$[0].skills[0]").value("Angular"));

        verify(mockService, times(1)).getAllJobListings();
    }
}

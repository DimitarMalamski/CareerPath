package com.careerpath.interfaces.api;

import com.careerpath.application.dto.JobDetailsDto;
import com.careerpath.application.dto.JobListingDto;
import com.careerpath.application.dto.JobRecommendationDto;
import com.careerpath.application.service.AiJobMatchingService;
import com.careerpath.application.service.JobDetailsApplicationService;
import com.careerpath.application.service.JobListingApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {
    private final JobListingApplicationService jobListingService;
    private final AiJobMatchingService aiJobMatchingService;
    private final JobDetailsApplicationService jobDetailsApplicationService;

    @GetMapping
    public List<JobListingDto> getAllJobListings() {
        return jobListingService.getAllJobListings();
    }

    @GetMapping("/{id}")
    public JobListingDto getJobListing(@PathVariable UUID id) {
        return jobListingService.getJobListingById(id);
    }

    @GetMapping("/{jobId}/details/{userId}")
    public JobDetailsDto getJobDetails(
            @PathVariable UUID jobId,
            @PathVariable String userId
    ) {
        return jobDetailsApplicationService.getJobDetails(jobId, userId);
    }

    @GetMapping("/recommendations/{userId}")
    public List<JobRecommendationDto> recommendJobsListings(@PathVariable String userId) {
        return aiJobMatchingService.getRecommendations(userId);
    }
}

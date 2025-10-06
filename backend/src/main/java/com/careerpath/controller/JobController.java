package com.careerpath.controller;

import com.careerpath.dto.JobListingDto;
import com.careerpath.service.JobListingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {
    private final JobListingService jobListingService;

    public JobController(JobListingService jobListingService) {
        this.jobListingService = jobListingService;
    }

    @GetMapping
    public List<JobListingDto> getAllJobListings() {
        return jobListingService.getAll();
    }
}

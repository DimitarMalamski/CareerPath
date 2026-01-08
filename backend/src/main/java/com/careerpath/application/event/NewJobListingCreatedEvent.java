package com.careerpath.application.event;

import com.careerpath.application.dto.JobListingDto;

public record NewJobListingCreatedEvent(
    JobListingDto job
) { }

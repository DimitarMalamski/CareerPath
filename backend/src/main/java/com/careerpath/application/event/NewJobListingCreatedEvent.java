package com.careerpath.application.event;

import java.util.UUID;

public record NewJobListingCreatedEvent(
    UUID jobId
) { }

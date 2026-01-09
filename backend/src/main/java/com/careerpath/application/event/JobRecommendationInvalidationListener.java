package com.careerpath.application.event;

import com.careerpath.application.service.AiJobMatchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobRecommendationInvalidationListener {

    private final AiJobMatchingService aiJobMatchingService;

    @EventListener
    public void onNewJobListingCreated(NewJobListingCreatedEvent event) {
        log.info(
                "New job listing created (id={}), invalidating recommendations cache",
                event.job().id()
        );

        aiJobMatchingService.invalidateAll();
    }
}
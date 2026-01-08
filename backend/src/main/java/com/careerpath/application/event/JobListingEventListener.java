package com.careerpath.application.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobListingEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void onNewJobListingCreated(NewJobListingCreatedEvent event) {
        log.info("New job listing created with id: {}", event.job().id());

        messagingTemplate.convertAndSend(
                "/topic/jobs",
                event.job()
        );
    }
}

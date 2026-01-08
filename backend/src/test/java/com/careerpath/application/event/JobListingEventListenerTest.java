package com.careerpath.application.event;

import com.careerpath.application.dto.JobListingDto;
import com.careerpath.domain.model.enums.JobStatus;
import com.careerpath.domain.model.enums.JobType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JobListingEventListenerTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private JobListingEventListener listener;

    @Test
    void onNewJobListingCreated_shouldSendWebSocketMessage() {
        JobListingDto jobDto = new JobListingDto(
                UUID.randomUUID(),
                "Java Dev",
                "Google",
                "Eindhoven",
                "Java stack",
                JobType.FULL_TIME,
                JobStatus.PUBLISHED,
                null,
                List.of("Java")
        );

        NewJobListingCreatedEvent event =
                new NewJobListingCreatedEvent(jobDto);

        listener.onNewJobListingCreated(event);

        verify(messagingTemplate)
                .convertAndSend("/topic/jobs", jobDto);
    }
}


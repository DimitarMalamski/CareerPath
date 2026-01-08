package com.careerpath.infrastructure.ai;

import com.careerpath.BaseIntegrationTest;
import com.careerpath.domain.model.JobMatchResult;
import com.careerpath.domain.model.Profile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@TestPropertySource(properties = {
        "ai.enabled=true",
        "spring.ai.openai.api-key=dummy",
        "spring.ai.openai.project-id=dummy",
        "spring.ai.openai.organization-id=dummy"
})
class AiJobMatcherAdapterIntegrationTest extends BaseIntegrationTest {

    @MockBean(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient openAiWebClient;

    @Autowired
    private AiJobMatcherAdapter aiJobMatcherAdapter;

    private Profile dummyProfile;
    private List<JobMatchResult> jobMatches;

    @BeforeEach
    void setup() {
        Mockito.when(
                openAiWebClient
                        .post()
                        .uri("/responses")
                        .bodyValue(any())
                        .retrieve()
                        .bodyToMono(String.class)
        ).thenReturn(
                Mono.just("""
                {
                  "output": [
                    {
                      "content": [
                        {
                          "text": "[{ \\"jobId\\": \\"job-1\\", \\"aiScore\\": 80, \\"aiExplanation\\": \\"Strong Java match.\\" }]"
                        }
                      ]
                    }
                  ]
                }
                """)
        );

        dummyProfile = new Profile();
        dummyProfile.setLocation("Eindhoven");

        jobMatches = List.of(
                JobMatchResult.builder()
                        .jobListingId("job-1")
                        .jobTitle("Java Backend Developer")
                        .description("Java + Spring Boot project")
                        .score(0.70)
                        .build(),
                JobMatchResult.builder()
                        .jobListingId("job-2")
                        .jobTitle("React Frontend Developer")
                        .description("React + TypeScript")
                        .score(0.10)
                        .build()
        );
    }

    @Test
    void enhanceMatches_shouldMergeAiScoresCorrectly() {
        // Act
        List<JobMatchResult> result =
                aiJobMatcherAdapter.enhanceMatches(dummyProfile, jobMatches);

        // Assert
        JobMatchResult first = result.get(0);
        JobMatchResult second = result.get(1);

        assertThat(first.getFinalScore()).isBetween(0.70, 0.90);
        assertThat(first.getAiExplanation()).isEqualTo("Strong Java match.");

        assertThat(second.getFinalScore()).isEqualTo(0.10);
        assertThat(second.getAiExplanation()).isNull();
    }

    @Test
    void enhanceMatches_shouldFallbackWhenJsonInvalid() {
        Mockito.when(
                openAiWebClient
                        .post()
                        .uri("/responses")
                        .bodyValue(any())
                        .retrieve()
                        .bodyToMono(String.class)
        ).thenReturn(
                Mono.just("""
                {
                  "output": [
                    {
                      "content": [
                        { "text": "NOT JSON AT ALL" }
                      ]
                    }
                  ]
                }
                """)
        );

        // Act
        List<JobMatchResult> result =
                aiJobMatcherAdapter.enhanceMatches(dummyProfile, jobMatches);

        // Assert
        assertThat(result.get(0).getAiExplanation())
                .isEqualTo("AI explanation unavailable (fallback).");
        assertThat(result.get(1).getAiExplanation())
                .isEqualTo("AI explanation unavailable (fallback).");
    }
}


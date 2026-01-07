package com.careerpath.infrastructure.ai;

import com.careerpath.domain.model.JobMatchResult;
import com.careerpath.domain.model.Profile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AiJobMatcherAdapterTest {

    private WebClient webClient;
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    private WebClient.RequestBodySpec requestBodySpec;
    private WebClient.ResponseSpec responseSpec;

    private AiJobMatcherAdapter adapter;

    @BeforeEach
    void setup() {
        webClient = mock(WebClient.class);
        requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        requestBodySpec = mock(WebClient.RequestBodySpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);

        adapter = new AiJobMatcherAdapter(webClient);

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
        doReturn(requestBodySpec)
                .when(requestBodySpec)
                .bodyValue(any());
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void enhanceMatches_successfullyAppliesAiScore() {
        // Arrange
        Profile profile = Profile.builder().build();

        JobMatchResult job = JobMatchResult.builder()
                .jobListingId("job-1")
                .score(0.5)
                .finalScore(0.5)
                .jobTitle("Backend Dev")
                .description("Java")
                .build();

        String aiResponse = """
        {
          "output": [
            {
              "content": [
                {
                  "type": "output_text",
                  "text": "[{ \\"jobId\\": \\"job-1\\", \\"aiScore\\": 80, \\"aiExplanation\\": \\"Strong Java match\\" }]"
                }
              ]
            }
          ]
        }
        """;

        when(responseSpec.bodyToMono(String.class))
                .thenReturn(Mono.just(aiResponse));

        // Act
        List<JobMatchResult> result =
                adapter.enhanceMatches(profile, List.of(job));

        // Assert
        JobMatchResult enhanced = result.get(0);

        assertThat(enhanced.getFinalScore()).isGreaterThan(0.5);
        assertThat(enhanced.getAiExplanation()).isEqualTo("Strong Java match");
    }

    @Test
    void enhanceMatches_fallsBackWhenAiFails() {
        // Arrange
        Profile profile = Profile.builder().build();

        JobMatchResult job = JobMatchResult.builder()
                .jobListingId("job-1")
                .score(0.4)
                .finalScore(0.4)
                .build();

        when(responseSpec.bodyToMono(String.class))
                .thenReturn(Mono.error(new RuntimeException("AI down")));

        // Act
        List<JobMatchResult> result =
                adapter.enhanceMatches(profile, List.of(job));

        // Assert
        JobMatchResult fallback = result.get(0);

        assertThat(fallback.getFinalScore()).isEqualTo(0.4);
        assertThat(fallback.getAiExplanation()).contains("unavailable");
    }

    @Test
    void enhanceMatches_onlyEnhancesTop5() {
        // Arrange
        Profile profile = Profile.builder().build();

        List<JobMatchResult> jobs = List.of(
                job("1", 0.9),
                job("2", 0.8),
                job("3", 0.7),
                job("4", 0.6),
                job("5", 0.5),
                job("6", 0.1)
        );

        String aiResponse = """
        {
          "output": [
            {
              "content": [
                {
                  "type": "output_text",
                  "text": "[{ \\"jobId\\": \\"1\\", \\"aiScore\\": 10, \\"aiExplanation\\": \\"ok\\" }]"
                }
              ]
            }
          ]
        }
        """;

        when(responseSpec.bodyToMono(String.class))
                .thenReturn(Mono.just(aiResponse));

        // Act
        List<JobMatchResult> result =
                adapter.enhanceMatches(profile, jobs);

        // Assert
        JobMatchResult untouched = result.get(5);
        assertThat(untouched.getFinalScore()).isEqualTo(0.1);
    }

    private JobMatchResult job(String id, double score) {
        return JobMatchResult.builder()
                .jobListingId(id)
                .score(score)
                .finalScore(score)
                .jobTitle("t")
                .description("d")
                .build();
    }
}

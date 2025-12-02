package com.careerpath.infrastructure.ai;

import com.careerpath.domain.model.JobMatchResult;
import com.careerpath.domain.model.Profile;
import com.careerpath.domain.model.ProfileSkill;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AiJobMatcherAdapterTest {

    private OpenAiService openAiService;
    private AiJobMatcherAdapter aiAdapter;

    private Profile profile;

    @BeforeEach
    void setup() {
        openAiService = mock(OpenAiService.class);
        aiAdapter = new AiJobMatcherAdapter(openAiService);

        profile = Profile.builder()
                .skills(List.of(
                        ProfileSkill.builder()
                                .id("1L")
                                .name("Java")
                                .level("Expert")
                                .build()
                ))
                .build();
    }

    @Test
    void enhanceMatches_shouldReturnEnhancedScores_andAiExplanation() {
        // Arrange
        JobMatchResult job = JobMatchResult.builder()
                .jobListingId(UUID.randomUUID().toString())
                .jobTitle("Backend Developer")
                .description("Java API backend")
                .score(0.6)
                .finalScore(0.6)
                .build();

        String expectedJson = """
            [
              {
                "jobId": "%s",
                "aiScore": 90,
                "aiExplanation": "Strong Java match"
              }
            ]
        """.formatted(job.getJobListingId());

        ChatMessage message = new ChatMessage("assistant", expectedJson);
        ChatCompletionChoice choice = new ChatCompletionChoice();
        choice.setMessage(message);

        ChatCompletionResult result = new ChatCompletionResult();
        result.setChoices(List.of(choice));

        when(openAiService.createChatCompletion(any(ChatCompletionRequest.class)))
                .thenReturn(result);

        // Act
        List<JobMatchResult> enhanced = aiAdapter.enhanceMatches(profile, List.of(job));

        // Assert
        assertThat(enhanced).hasSize(1);
        JobMatchResult enhancedJob = enhanced.get(0);

        assertThat(enhancedJob.getFinalScore()).isGreaterThan(job.getScore());
        assertThat(enhancedJob.getAiExplanation()).isEqualTo("Strong Java match");

        verify(openAiService, times(1)).createChatCompletion(any());
    }

    @Test
    void enhanceMatches_shouldFallback_whenAiFails() {
        // Arrange
        JobMatchResult job = JobMatchResult.builder()
                .jobListingId("123")
                .score(0.5)
                .build();

        when(openAiService.createChatCompletion(any(ChatCompletionRequest.class)))
                .thenThrow(new RuntimeException("API offline"));

        // Act
        List<JobMatchResult> enhanced = aiAdapter.enhanceMatches(profile, List.of(job));

        // Assert
        JobMatchResult result = enhanced.get(0);
        assertThat(result.getFinalScore()).isEqualTo(job.getScore());
        assertThat(result.getAiExplanation()).contains("unavailable");

        verify(openAiService, times(1)).createChatCompletion(any());
    }

    @Test
    void enhanceMatches_shouldLimitToTop5Jobs() {
        // Arrange
        List<JobMatchResult> jobs = List.of(
                job("1", 0.9),
                job("2", 0.7),
                job("3", 0.5),
                job("4", 0.4),
                job("5", 0.3),
                job("6", 0.1)
        );

        String json = """
        [
          {"jobId": "1", "aiScore": 10, "aiExplanation": "ok"},
          {"jobId": "2", "aiScore": 10, "aiExplanation": "ok"},
          {"jobId": "3", "aiScore": 10, "aiExplanation": "ok"},
          {"jobId": "4", "aiScore": 10, "aiExplanation": "ok"},
          {"jobId": "5", "aiScore": 10, "aiExplanation": "ok"}
        ]
        """;

        ChatMessage message = new ChatMessage("assistant", json);
        ChatCompletionChoice choice = new ChatCompletionChoice();
        choice.setMessage(message);

        ChatCompletionResult result = new ChatCompletionResult();
        result.setChoices(List.of(choice));

        when(openAiService.createChatCompletion(any())).thenReturn(result);

        // Act
        List<JobMatchResult> enhanced = aiAdapter.enhanceMatches(profile, jobs);

        // Assert
        assertThat(enhanced).hasSize(6);
        assertThat(enhanced.get(5).getJobListingId()).isEqualTo("6");
        assertThat(enhanced.get(5).getFinalScore()).isEqualTo(0.1);

        verify(openAiService, times(1)).createChatCompletion(any());
    }

    private JobMatchResult job(String id, double score) {
        return JobMatchResult.builder()
                .jobListingId(id)
                .score(score)
                .finalScore(score)
                .jobTitle("t")
                .company("c")
                .description("d")
                .matchedSkills(List.of())
                .missingSkills(List.of())
                .build();
    }
}

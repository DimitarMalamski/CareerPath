package com.careerpath.infrastructure.ai;

import com.careerpath.BaseIntegrationTest;
import com.careerpath.domain.model.JobMatchResult;
import com.careerpath.domain.model.Profile;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.service.OpenAiService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class AiJobMatcherAdapterIntegrationTest extends BaseIntegrationTest {

    @MockBean
    private OpenAiService openAiService;

    @Autowired
    private AiJobMatcherAdapter aiJobMatcherAdapter;

    private Profile dummyProfile;
    private List<JobMatchResult> jobMatches;

    @BeforeEach
    void setup() {
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
        String jsonResponse = """
            [
              { "jobId": "job-1", "aiScore": 80, "aiExplanation": "Strong Java match." },
              { "jobId": "job-2", "aiScore": 20, "aiExplanation": "Weak frontend match." }
            ]
        """;

        ChatMessage message = new ChatMessage("assistant", jsonResponse);
        ChatCompletionChoice choice = new ChatCompletionChoice();
        choice.setMessage(message);

        ChatCompletionResult aiResult = new ChatCompletionResult();
        aiResult.setChoices(List.of(choice));

        Mockito.when(openAiService.createChatCompletion(any())).thenReturn(aiResult);

        // Act
        List<JobMatchResult> result = aiJobMatcherAdapter.enhanceMatches(dummyProfile, jobMatches);

        // Assert
        JobMatchResult first = result.get(0);
        JobMatchResult second = result.get(1);

        assertThat(first.getFinalScore()).isBetween(0.70, 0.90);
        assertThat(first.getAiExplanation()).isEqualTo("Strong Java match.");

        assertThat(second.getFinalScore()).isBetween(0.10, 0.30);
        assertThat(second.getAiExplanation()).isEqualTo("Weak frontend match.");
    }

    @Test
    void enhanceMatches_shouldFallbackWhenJsonInvalid() {
        String badJson = "NOT JSON";

        ChatMessage message = new ChatMessage("assistant", badJson);
        ChatCompletionChoice choice = new ChatCompletionChoice();
        choice.setMessage(message);

        ChatCompletionResult aiResult = new ChatCompletionResult();
        aiResult.setChoices(List.of(choice));

        Mockito.when(openAiService.createChatCompletion(any())).thenReturn(aiResult);

        // Act
        List<JobMatchResult> result = aiJobMatcherAdapter.enhanceMatches(dummyProfile, jobMatches);

        // Assert fallback
        assertThat(result.get(0).getAiExplanation()).isEqualTo("AI enhancement unavailable.");
        assertThat(result.get(1).getAiExplanation()).isEqualTo("AI enhancement unavailable.");
    }
}

package com.careerpath.infrastructure.ai;

import com.careerpath.application.dto.AiEnhancementResult;
import com.careerpath.domain.model.JobMatchResult;
import com.careerpath.domain.model.Profile;
import com.careerpath.domain.port.AiJobMatcherPort;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AiJobMatcherAdapter implements AiJobMatcherPort {

    private final OpenAiService openAiService;
    private static final String MODEL = "gpt-4o-mini";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<JobMatchResult> enhanceMatches(Profile profile, List<JobMatchResult> matches) {

        List<JobMatchResult> topMatches = matches.stream()
                .sorted(Comparator.comparingDouble(JobMatchResult::getScore).reversed())
                .limit(5)
                .toList();

        List<AiEnhancementResult> aiResults = batchAiEnhance(profile, topMatches);

        for (JobMatchResult job : topMatches) {
            aiResults.stream()
                    .filter(ai -> ai.jobId().equals(job.getJobListingId()))
                    .findFirst()
                    .ifPresent(ai -> {
                        double finalScore = mergeScores(job.getScore(), ai.aiScore());
                        job.setFinalScore(finalScore);
                        job.setAiExplanation(ai.aiExplanation());
                    });
        }

        return matches;
    }

    private List<AiEnhancementResult> batchAiEnhance(Profile profile, List<JobMatchResult> topMatches) {
        try {
            String prompt = buildBatchPrompt(profile, topMatches);

            ChatCompletionRequest request = ChatCompletionRequest.builder()
                    .model(MODEL)
                    .temperature(0.0)
                    .maxTokens(500)
                    .messages(List.of(new ChatMessage("user", prompt)))
                    .build();

            ChatCompletionResult result = openAiService.createChatCompletion(request);
            String json = result.getChoices().get(0).getMessage().getContent()
                    .replace("```json", "")
                    .replace("```", "")
                    .trim();

            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (Exception e) {
            return topMatches.stream()
                    .map(job -> new AiEnhancementResult(
                            job.getJobListingId(),
                            (int) (job.getScore() * 100),
                            "AI enhancement unavailable."
                    ))
                    .toList();
        }
    }

    private String buildBatchPrompt(Profile profile, List<JobMatchResult> jobs) {

        StringBuilder sb = new StringBuilder("""
            You are an AI job-matching engine.
            For each job, return a JSON array with:
            - jobId
            - aiScore (0–100)
            - aiExplanation (1–2 short sentences)

            PROFILE:
        """);

        sb.append(profile.toString()).append("\n\nJOBS:\n");

        for (JobMatchResult job : jobs) {
            sb.append("""
            {
              "jobId": "%s",
              "title": "%s",
              "description": "%s",
              "baselineScore": %.2f
            },
            """.formatted(
                    job.getJobListingId(),
                    safe(job.getJobTitle()),
                    safe(job.getDescription()),
                    job.getScore()
            ));
        }

        sb.append("""    
            Return ONLY a valid JSON array.
            Do not wrap it in ```json or any other markdown.
            Do not include explanations, comments, or text outside the JSON.
            [
              { "jobId": "...", "aiScore": 87, "aiExplanation": "..." },
              ...
            ]
        """);

        return sb.toString();
    }

    private String safe(String value) {
        return value == null ? "" : value.replace("\"", "'");
    }

    private double mergeScores(double baseline, int aiScore) {
        double aiNorm = aiScore / 100.0;
        return normalizeScore((baseline * 0.6) + (aiNorm * 0.4));
    }

    private double normalizeScore(double score) {
        return Math.max(0, Math.min(1, score));
    }
}

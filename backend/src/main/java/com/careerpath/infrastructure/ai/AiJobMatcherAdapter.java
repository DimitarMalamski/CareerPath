package com.careerpath.infrastructure.ai;

import com.careerpath.application.dto.AiEnhancementResult;
import com.careerpath.domain.model.JobMatchResult;
import com.careerpath.domain.model.Profile;
import com.careerpath.domain.port.AiJobMatcherPort;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "ai.enabled", havingValue = "true")
public class AiJobMatcherAdapter implements AiJobMatcherPort {

    private static final String MODEL = "gpt-4o-mini";

    private final WebClient openAiWebClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<JobMatchResult> enhanceMatches(Profile profile, List<JobMatchResult> matches) {
        System.out.println(">>> AI enhanceMatches() ENTERED");

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

            Map<String, Object> requestBody = Map.of(
                    "model", MODEL,
                    "input", List.of(
                            Map.of(
                                    "role", "user",
                                    "content", List.of(
                                            Map.of(
                                                    "type", "input_text",
                                                    "text", prompt
                                            )
                                    )
                            )
                    ),
                    "max_output_tokens", 300
            );

            String response = openAiWebClient.post()
                    .uri("/responses")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode root = objectMapper.readTree(response);

            // ✅ Only fail if error is NOT null
            if (root.has("error") && !root.get("error").isNull()) {
                throw new IllegalStateException(
                        "OpenAI error: " + root.path("error").path("message").asText()
                );
            }

            String content = extractOutputText(root);

            return objectMapper.readValue(
                    content,
                    new TypeReference<List<AiEnhancementResult>>() {}
            );

        } catch (Exception e) {
            System.err.println(">>> AI ENHANCEMENT FAILED");
            e.printStackTrace();

            return topMatches.stream()
                    .map(job -> new AiEnhancementResult(
                            job.getJobListingId(),
                            (int) (job.getScore() * 100),
                            "AI enhancement unavailable."
                    ))
                    .toList();
        }
    }

    /**
     * Extracts the assistant text from OpenAI Responses API safely.
     */
    private String extractOutputText(JsonNode root) {
        JsonNode output = root.path("output");

        if (!output.isArray() || output.isEmpty()) {
            throw new IllegalStateException("OpenAI response missing output array");
        }

        JsonNode content = output.get(0).path("content");

        if (!content.isArray() || content.isEmpty()) {
            throw new IllegalStateException("OpenAI response missing content array");
        }

        return content.get(0).path("text").asText();
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

        sb.append(profile).append("\n\nJOBS:\n");

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
            Do not wrap it in markdown.
            Do not include any text outside JSON.
            [
              { "jobId": "...", "aiScore": 87, "aiExplanation": "..." }
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

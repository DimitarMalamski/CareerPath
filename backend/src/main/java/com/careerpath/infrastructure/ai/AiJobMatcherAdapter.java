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

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "ai.enabled", havingValue = "true")
public class AiJobMatcherAdapter implements AiJobMatcherPort {

    private static final String MODEL = "gpt-4o-mini";
    private static final String OPENAI_ERROR_FIELD = "error";

    private final WebClient openAiWebClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<JobMatchResult> enhanceMatches(Profile profile, List<JobMatchResult> matches) {
        List<JobMatchResult> topMatches = matches.stream()
                .limit(3)
                .toList();

        List<AiEnhancementResult> aiResults = batchAiEnhance(profile, topMatches);

        for (JobMatchResult job : topMatches) {
            aiResults.stream()
                    .filter(ai -> ai.jobId().equals(job.getJobListingId()))
                    .findFirst()
                    .ifPresentOrElse(ai -> {
                        double finalScore = mergeScores(job.getScore(), ai.aiScore());
                        job.setFinalScore(finalScore);
                        job.setAiExplanation(ai.aiExplanation());
                    },
                    () -> {
                        job.setFinalScore(job.getScore());
                        job.setAiExplanation(null);
                    }
            );
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
                    "max_output_tokens", 500
            );

            String response = openAiWebClient.post()
                    .uri("/responses")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode root = objectMapper.readTree(response);

            if (root.has("status") && "incomplete".equals(root.get("status").asText())) {
                String reason = root.path("incomplete_details").path("reason").asText("unknown");
                throw new IllegalStateException("OpenAI response incomplete: " + reason);
            }

            if (root.has(OPENAI_ERROR_FIELD) && !root.get(OPENAI_ERROR_FIELD).isNull()) {
                throw new IllegalStateException(
                        "OpenAI error: " +
                                root.path(OPENAI_ERROR_FIELD).path("message").asText()
                );
            }

            String content = extractOutputText(root);

            return safeParseAiResponse(content, topMatches);

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

    /**
     * Extracts the assistant text from OpenAI Responses API safely.
     */
    private String extractOutputText(JsonNode root) {
        JsonNode output = root.path("output");

        if (!output.isArray() || output.isEmpty()) {
            throw new IllegalStateException("OpenAI response missing output array");
        }

        StringBuilder sb = new StringBuilder();

        for (JsonNode message : output) {
            JsonNode contentArray = message.path("content");

            if (!contentArray.isArray()) continue;

            for (JsonNode content : contentArray) {
                if (content.has("text")) {
                    sb.append(content.get("text").asText());
                }
                if (content.has("output_text")) {
                    sb.append(content.get("output_text").asText());
                }
            }
        }

        String result = sb.toString().trim();

        if (result.isEmpty()) {
            throw new IllegalStateException("OpenAI response contained no text");
        }

        return result;
    }

    private String buildBatchPrompt(Profile profile, List<JobMatchResult> jobs) {
        StringBuilder sb = new StringBuilder("""
            You are an AI job-matching engine.
            For each job, return a JSON array with:
            - jobId
            - aiScore (0–100)
            - aiExplanation (ONE short sentence, max 20 words)

            PROFILE:
        """);

        sb.append(profile).append("\n\nJOBS:\n");

        for (int i = 0; i < jobs.size(); i++) {
            JobMatchResult job = jobs.get(i);

            sb.append("""
                {
                  "jobId": "%s",
                  "title": "%s",
                  "description": "%s",
                  "baselineScore": %.2f
                }
                """.formatted(
                    job.getJobListingId(),
                    safe(job.getJobTitle()),
                    safe(job.getDescription()).substring(0, Math.min(300, job.getDescription().length())),
                    job.getScore()
            ));

            if (i < jobs.size() - 1) {
                sb.append(",\n");
            }
        }

        sb.append("""
            You MUST return a valid JSON array.
            Rules:
            - Use double quotes only
            - Escape all quotes inside text
            - Do not break lines inside strings
            - Do not include trailing commas
            - Do not include any text outside JSON
        
            Valid example:
            [
              {
                "jobId": "uuid",
                "aiScore": 85,
                "aiExplanation": "Strong match due to Java and Spring experience."
              }
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

    private List<AiEnhancementResult> safeParseAiResponse(
            String content,
            List<JobMatchResult> topMatches
    ) {
        try {
            objectMapper.readTree(content);

            return objectMapper.readValue(
                    content,
                    new TypeReference<>() {
                    }
            );
        } catch (Exception e) {
            return topMatches.stream()
                    .map(job -> new AiEnhancementResult(
                            job.getJobListingId(),
                            (int) (job.getScore() * 100),
                            "AI explanation unavailable (fallback)."
                    ))
                    .toList();
        }
    }
}

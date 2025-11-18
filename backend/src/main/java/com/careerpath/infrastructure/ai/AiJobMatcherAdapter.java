package com.careerpath.infrastructure.ai;

import com.careerpath.domain.model.JobMatchResult;
import com.careerpath.domain.model.Profile;
import com.careerpath.domain.model.ProfileSkill;
import com.careerpath.domain.port.AiJobMatcherPort;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AiJobMatcherAdapter implements AiJobMatcherPort {
    private final OpenAiService openAiService;

    @Override
    public List<JobMatchResult> enhanceMatches(Profile profile, List<JobMatchResult> matches) {

        List<JobMatchResult> topMatches = matches.stream()
                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                .limit(5)
                .toList();

        for (JobMatchResult match : topMatches) {
            String prompt = buildPrompt(profile, match);

            try {
                ChatCompletionRequest request = ChatCompletionRequest.builder()
                        .model("gpt-4o-mini")
                        .temperature(0.2)
                        .maxTokens(150)
                        .messages(List.of(
                                new ChatMessage("user", prompt)
                        ))
                        .build();

                ChatCompletionResult result = openAiService.createChatCompletion(request);

                String aiText = result.getChoices().get(0).getMessage().getContent();

                match.setAiExplanation(aiText);

                double aiBoost = calculateAiBoost(aiText);

                double finalScore = normalizeScore(match.getScore() + aiBoost);
                match.setFinalScore(finalScore);

            } catch (Exception e) {
                match.setAiExplanation("AI explanation unavailable.");
                match.setFinalScore(match.getScore());
            }
        }

        return matches;
    }

    private String buildPrompt(Profile profile, JobMatchResult match) {
        return """
                You are a job-matching assistant.

                USER PROFILE:
                - Name: %s
                - Headline: %s
                - Location: %s
                - Skills: %s

                JOB MATCH:
                - Job title: %s
                - Company: %s
                - Job description: %s
                - Baseline match score: %.2f
                - Reasoning: %s

                TASK:
                Provide a short, clear explanation (max 3 sentences)
                of why this job matches the user.
                Write professionally. Do not repeat identical phrases across jobs.
                """.formatted(
                profile.getFullName(),
                safe(profile.getHeadline()),
                safe(profile.getLocation()),
                profile.getSkills()
                        .stream()
                        .map(ProfileSkill::getName)
                                .collect(Collectors.joining(", ")),
                match.getJobTitle(),
                match.getCompany(),
                safe(match.getDescription()),
                match.getScore(),
                safe(match.getExplanation())
        );
    }

    private String safe(String value) {
        return value == null ? "unknown" : value;
    }

    private double calculateAiBoost(String aiText) {
        if (aiText == null) return 0.0;

        String t = aiText.toLowerCase();

        if (t.contains("aligns well") ||
                t.contains("strong match") ||
                t.contains("well suited") ||
                t.contains("excellent fit")) {
            return 0.2;
        }

        if (t.contains("not a match") ||
                t.contains("does not align") ||
                t.contains("unlikely fit") ||
                t.contains("poor fit")) {
            return -0.2;
        }

        return 0.0;
    }

    private double normalizeScore(double score) {
        if (score < 0) return 0;
        if (score > 1) return 1;
        return score;
    }
}

package com.careerpath.infrastructure.ai;

import com.careerpath.domain.model.JobMatchResult;
import com.careerpath.domain.model.Profile;
import com.careerpath.domain.port.AiJobMatcherPort;
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

    @Override
    public List<JobMatchResult> enhanceMatches(Profile profile, List<JobMatchResult> matches) {

        List<JobMatchResult> topMatches = matches.stream()
                .sorted(Comparator.comparingDouble(JobMatchResult::getScore).reversed())
                .limit(5)
                .toList();

        for (JobMatchResult match : topMatches) {
            try {
                int aiScore = generateAiScore(profile, match);
                String explanation = generateAiExplanation(profile, match);

                match.setAiExplanation(explanation);

                double finalScore = mergeScores(match.getScore(), aiScore);
                match.setFinalScore(finalScore);

            } catch (Exception e) {
                match.setAiExplanation("AI explanation unavailable.");
                match.setFinalScore(match.getScore());
            }
        }

        return matches;
    }

    private int generateAiScore(Profile profile, JobMatchResult match) {
        String prompt = buildScorePrompt(profile, match);

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(MODEL)
                .temperature(0.0)
                .maxTokens(5)
                .messages(List.of(new ChatMessage("user", prompt)))
                .build();

        ChatCompletionResult result = openAiService.createChatCompletion(request);
        String content = result.getChoices().get(0).getMessage().getContent();

        return parseScore(content);
    }

    private String generateAiExplanation(Profile profile, JobMatchResult match) {
        String prompt = buildExplanationPrompt(profile, match);

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(MODEL)
                .temperature(0.2)
                .maxTokens(80)
                .messages(List.of(new ChatMessage("user", prompt)))
                .build();

        ChatCompletionResult result = openAiService.createChatCompletion(request);
        return result.getChoices().get(0).getMessage().getContent();
    }

    private String buildScorePrompt(Profile profile, JobMatchResult match) {
        return """
        You are an AI job-matching engine.
        
        Rate how well this job matches the candidate on a scale from 0 to 100.
        Consider ONLY:
        - skill alignment
        - experience alignment
        - seniority match
        - technology/stack matching
        - location fit (if mentioned)
        
        USER PROFILE:
        %s
    
        JOB LISTING:
        %s
    
        Return ONLY a single integer number between 0 and 100.
        No explanations.
        """.formatted(
                profile.toString(),
                match.getDescription()
        );
    }

    private String buildExplanationPrompt(Profile profile, JobMatchResult match) {
        return """
        Explain in one short paragraph why this job is a good or bad match
        for the candidate based on skills, experience, and stack.
    
        USER PROFILE:
        %s
    
        JOB LISTING:
        %s
    
        Keep it professional and concise.
        Limit your answer to 2–3 sentences.
        """.formatted(profile.toString(), match.getDescription());
    }

    private double mergeScores(double baseline, int aiScore) {
        double aiNorm = aiScore / 100.0;
        return normalizeScore((baseline * 0.6) + (aiNorm * 0.4));
    }

    private double normalizeScore(double score) {
        return Math.max(0, Math.min(1, score));
    }

    private int parseScore(String value) {
        try {
            return Integer.parseInt(value.replaceAll("\\D", ""));
        } catch (Exception e) {
            return 50;
        }
    }
}

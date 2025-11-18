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

import java.util.List;

@Component
@RequiredArgsConstructor
public class AiJobMatcherAdapter implements AiJobMatcherPort {
    private final OpenAiService openAi;

    @Override
    public List<JobMatchResult> enhanceMatches(Profile profile, List<JobMatchResult> matches) {

        if (matches.isEmpty()) return matches;

        String prompt = buildPrompt(profile, matches);

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-4o-mini")
                .messages(List.of(
                        new ChatMessage("user", prompt)
                ))
                .temperature(0.2)
                .build();

        ChatCompletionResult result = openAi.createChatCompletion(request);

        String response = result.getChoices().get(0).getMessage().getContent();

        for (JobMatchResult m : matches) {
            m.setAiExplanation(response);
        }

        return matches;
    }

    private String buildPrompt(Profile p, List<JobMatchResult> m) {
        StringBuilder sb = new StringBuilder();
        sb.append("Improve this job match explanation:\n\n");

        for (JobMatchResult r : m) {
            sb.append("- ").append(r.getExplanation()).append("\n");
        }

        return sb.toString();
    }
}

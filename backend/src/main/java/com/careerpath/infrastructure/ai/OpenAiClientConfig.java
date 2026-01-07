package com.careerpath.infrastructure.ai;

import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConditionalOnProperty(name = "ai.enabled", havingValue = "true")
public class OpenAiClientConfig {

    @Bean
    public OpenAiService openAiService(
            @Value("${spring.ai.openai.api-key}") String apiKey
    ) {
        System.out.println(">>> OpenAiService bean CREATED");
        System.out.println(">>> API KEY PRESENT: " + (apiKey != null && !apiKey.isBlank()));

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("OpenAI API key is missing");
        }

        return new OpenAiService(apiKey, Duration.ofSeconds(10));
    }
}

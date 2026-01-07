package com.careerpath.infrastructure.ai;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
@ConditionalOnProperty(name = "ai.enabled", havingValue = "true")
public class OpenAiHttpClientConfig {

    private static final String OPENAI_BASE_URL = "https://api.openai.com/v1";

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Value("${spring.ai.openai.project-id}")
    private String projectId;

    @Value("${spring.ai.openai.organization-id}")
    private String organizationId;

    @PostConstruct
    void logConfigStatus() {
        log.info("OpenAI client enabled");
        log.debug("OpenAI apiKey present: {}", apiKey != null && !apiKey.isBlank());
        log.debug("OpenAI projectId: {}", projectId);
        log.debug("OpenAI organizationId: {}", organizationId);
    }

    @Bean
    WebClient openAiWebClient() {
        validateConfig();

        return WebClient.builder()
                .baseUrl(OPENAI_BASE_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader("OpenAI-Project", projectId)
                .defaultHeader("OpenAI-Organization", organizationId)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    private void validateConfig() {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("OpenAI API key is missing");
        }
        if (projectId == null || projectId.isBlank()) {
            throw new IllegalStateException("OpenAI Project ID is missing");
        }
        if (organizationId == null || organizationId.isBlank()) {
            throw new IllegalStateException("OpenAI Organization ID is missing");
        }
    }
}

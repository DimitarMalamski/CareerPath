package com.careerpath.infrastructure.ai;

import com.theokanning.openai.service.OpenAiService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class OpenAiClientConfig {

    @Bean
    public OpenAiService openAiService() {
        return new OpenAiService(System.getenv("OPENAI_API_KEY"), Duration.ofSeconds(10));
    }
}

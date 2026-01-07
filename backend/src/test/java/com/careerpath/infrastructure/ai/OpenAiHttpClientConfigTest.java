package com.careerpath.infrastructure.ai;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = OpenAiHttpClientConfig.class)
@TestPropertySource(properties = {
        "ai.enabled=true",
        "spring.ai.openai.api-key=dummy"
})
class OpenAiHttpClientConfigTest {

    @Autowired
    private WebClient openAiWebClient;

    @Test
    void webClientBean_shouldBeCreated() {
        assertThat(openAiWebClient).isNotNull();
    }
}

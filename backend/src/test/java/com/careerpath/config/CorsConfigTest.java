package com.careerpath.config;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.assertj.core.api.Assertions.assertThat;

class CorsConfigTest {

    @Test
    void corsConfigurerBean_shouldBeCreated_andApplyCorsMappings() {
        // Arrange
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(CorsConfig.class)) {

            // Act
            WebMvcConfigurer configurer = context.getBean(WebMvcConfigurer.class);
            CorsRegistry registry = new CorsRegistry();
            configurer.addCorsMappings(registry);

            // Assert
            assertThat(configurer).isNotNull();
        }
    }
}

package com.careerpath.config;

import com.careerpath.application.service.UserOnboardingService;
import com.careerpath.domain.port.ProfilePersistencePort;
import com.careerpath.domain.port.UserOnboardingPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

    @Bean
    public UserOnboardingPort userOnboardingPort(ProfilePersistencePort profileRepositoryPort) {
        return new UserOnboardingService(profileRepositoryPort);
    }
}

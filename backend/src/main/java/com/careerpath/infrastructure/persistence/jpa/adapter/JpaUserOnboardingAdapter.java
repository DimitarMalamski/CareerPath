package com.careerpath.infrastructure.persistence.jpa.adapter;

import com.careerpath.domain.port.ProfileRepositoryPort;
import com.careerpath.domain.port.UserOnboardingPort;
import com.careerpath.application.service.UserOnboardingService;
import org.springframework.stereotype.Component;

@Component
public class JpaUserOnboardingAdapter implements UserOnboardingPort {
    private final UserOnboardingService userOnboardingService;

    public JpaUserOnboardingAdapter(ProfileRepositoryPort profileRepositoryPort) {
        this.userOnboardingService = new UserOnboardingService(profileRepositoryPort);
    }

    @Override
    public void ensureUserProfile(String userId) {
        userOnboardingService.ensureUserProfile(userId);
    }
}

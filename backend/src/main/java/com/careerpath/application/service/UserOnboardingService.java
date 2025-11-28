package com.careerpath.application.service;

import com.careerpath.domain.model.Profile;
import com.careerpath.domain.port.ProfilePersistencePort;
import com.careerpath.domain.port.UserOnboardingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserOnboardingService implements UserOnboardingPort {

    private final ProfilePersistencePort profileRepositoryPort;

    @Override
    public void ensureUserProfile(String userId) {
        if (profileRepositoryPort.existsByUserId(userId)) {
            return;
        }

        Profile defaultProfile = Profile.builder()
                .userId(userId)
                .fullName("")
                .headline("")
                .about("")
                .location("")
                .skills(List.of())
                .experiences(List.of())
                .aiOptIn(true)
                .build();

        profileRepositoryPort.save(defaultProfile);
    }
}

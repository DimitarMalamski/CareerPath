package com.careerpath.application.service;

import com.careerpath.domain.model.Profile;
import com.careerpath.domain.port.ProfileRepositoryPort;
import com.careerpath.domain.port.UserOnboardingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserOnboardingService implements UserOnboardingPort {

    private final ProfileRepositoryPort profileRepositoryPort;

    @Override
    public void ensureUserProfile(String userId) {
        profileRepositoryPort.findByUserId(userId)
                .orElseGet(() -> {
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

                    return profileRepositoryPort.save(defaultProfile);
                });
    }
}

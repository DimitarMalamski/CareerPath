package com.careerpath.domain.service;

import com.careerpath.domain.model.Profile;
import com.careerpath.domain.port.ProfileRepositoryPort;
import com.careerpath.domain.port.UserOnboardingPort;

import java.util.ArrayList;

public class UserOnboardingService implements UserOnboardingPort {

    private final ProfileRepositoryPort profileRepositoryPort;

    public UserOnboardingService(ProfileRepositoryPort profileRepository) {
        this.profileRepositoryPort = profileRepository;
    }

    @Override
    public void ensureUserProfile(String userId) {
        profileRepositoryPort.findByUserId(userId).orElseGet(() -> {
            Profile defaultProfile = Profile.builder()
                    .userId(userId)
                    .fullName("New User")
                    .headline("")
                    .about("")
                    .location("")
                    .skills(new ArrayList<>())
                    .experiences(new ArrayList<>())
                    .build();
            return profileRepositoryPort.save(defaultProfile);
        });
    }
}

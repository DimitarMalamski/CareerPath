package com.careerpath.application.service;

import com.careerpath.application.dto.ProfileDto;
import com.careerpath.application.mapper.ProfileMapper;
import com.careerpath.domain.model.Profile;
import com.careerpath.domain.port.ProfilePersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileApplicationService {
    private final ProfilePersistencePort profilePersistencePort;

    public ProfileDto getProfile(String userId) {
        Profile profile = profilePersistencePort.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + userId));

        return ProfileMapper.toDto(profile);
    }

    public ProfileDto updateProfile(String userId, ProfileDto dto) {
        Profile existing = profilePersistencePort.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + userId));

        Profile updatedData = ProfileMapper.toDomain(userId, dto);

        existing.setFullName(updatedData.getFullName());
        existing.setHeadline(updatedData.getHeadline());
        existing.setAbout(updatedData.getAbout());
        existing.setLocation(updatedData.getLocation());
        existing.setSkills(updatedData.getSkills());
        existing.setExperiences(updatedData.getExperiences());
        existing.setAiOptIn(updatedData.isAiOptIn());

        Profile saved = profilePersistencePort.save(existing);

        return ProfileMapper.toDto(saved);
    }
}

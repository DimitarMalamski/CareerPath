package com.careerpath.infrastructure.persistence.jpa.adapter;

import com.careerpath.domain.model.Profile;
import com.careerpath.domain.port.ProfileRepositoryPort;
import com.careerpath.infrastructure.persistence.jpa.entity.ProfileEntity;
import com.careerpath.infrastructure.persistence.jpa.mapper.ProfileEntityMapper;
import com.careerpath.infrastructure.persistence.jpa.repository.SpringDataProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JpaProfileRepositoryAdapter implements ProfileRepositoryPort {
    private final SpringDataProfileRepository profileRepository;

    @Override
    public Profile getProfileByUserId(Long userId) {
        ProfileEntity profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found for userId: " + userId));

        return ProfileEntityMapper.toDomain(profile);
    }
}

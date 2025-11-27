package com.careerpath.infrastructure.persistence.jpa.adapter;

import com.careerpath.domain.model.Profile;
import com.careerpath.domain.port.ProfilePersistencePort;
import com.careerpath.infrastructure.persistence.jpa.entity.ProfileEntity;
import com.careerpath.infrastructure.persistence.jpa.mapper.ProfileEntityMapper;
import com.careerpath.infrastructure.persistence.jpa.repository.SpringDataProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JpaProfileRepositoryAdapter implements ProfilePersistencePort {
    private final SpringDataProfileRepository profileRepository;

    @Override
    public Optional<Profile> findByUserId(String userId) {
        return profileRepository.findByUserId(userId)
                .map(ProfileEntityMapper::toDomain);
    }

    @Override
    public Profile save(Profile profile) {
        ProfileEntity entity = ProfileEntityMapper.toEntity(profile);
        ProfileEntity saved = profileRepository.save(entity);
        return ProfileEntityMapper.toDomain(saved);
    }
}

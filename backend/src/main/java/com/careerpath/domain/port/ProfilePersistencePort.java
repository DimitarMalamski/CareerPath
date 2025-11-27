package com.careerpath.domain.port;

import com.careerpath.domain.model.Profile;

import java.util.Optional;

public interface ProfilePersistencePort {
    Optional<Profile> findByUserId(String userId);

    Profile save(Profile profile);
}

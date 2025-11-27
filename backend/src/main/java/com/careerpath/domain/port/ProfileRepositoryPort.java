package com.careerpath.domain.port;

import com.careerpath.domain.model.Profile;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRepositoryPort {
    Optional<Profile> findByUserId(String userId);

    Profile save(Profile profile);
}

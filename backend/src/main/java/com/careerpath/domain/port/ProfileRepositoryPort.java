package com.careerpath.domain.port;

import com.careerpath.domain.model.Profile;

public interface ProfileRepositoryPort {
    Profile getProfileByUserId(Long userId);
}

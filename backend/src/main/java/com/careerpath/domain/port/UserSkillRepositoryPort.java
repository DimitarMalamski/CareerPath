package com.careerpath.domain.port;

import com.careerpath.domain.model.UserSkill;

import java.util.List;
import java.util.UUID;

public interface UserSkillRepositoryPort {

    List<UserSkill> findByUserId(UUID userId);

    void saveAll(UUID userId, List<UserSkill> skills);

    void deleteByUserId(UUID userId);
}

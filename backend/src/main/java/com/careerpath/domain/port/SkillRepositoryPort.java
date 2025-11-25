package com.careerpath.domain.port;

import com.careerpath.domain.model.Skill;

import java.util.List;
import java.util.Optional;

public interface SkillRepositoryPort {

    // May be null
    Optional<Skill> findById(Integer id);

    List<Skill> findAll();
}

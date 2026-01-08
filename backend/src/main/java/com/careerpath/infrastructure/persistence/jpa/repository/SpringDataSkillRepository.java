package com.careerpath.infrastructure.persistence.jpa.repository;

import com.careerpath.infrastructure.persistence.jpa.entity.SkillEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataSkillRepository
        extends JpaRepository<SkillEntity, Integer> {

    Optional<SkillEntity> findByName(String name);
}
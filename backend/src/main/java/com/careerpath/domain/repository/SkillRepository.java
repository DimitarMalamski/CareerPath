package com.careerpath.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Integer> {
    boolean existsByName(String name);
    Optional<Skill> findByName(String name);
}

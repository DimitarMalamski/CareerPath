package com.careerpath.repository;

import com.careerpath.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Integer> {
    boolean existsByName(String name);
}

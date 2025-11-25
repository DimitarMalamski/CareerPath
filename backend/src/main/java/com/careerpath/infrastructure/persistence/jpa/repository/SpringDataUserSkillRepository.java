package com.careerpath.infrastructure.persistence.jpa.repository;

import com.careerpath.infrastructure.persistence.jpa.entity.UserSkillEntity;
import com.careerpath.infrastructure.persistence.jpa.entity.UserSkillIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataUserSkillRepository extends JpaRepository<UserSkillEntity, UserSkillIdEntity> {

    List<UserSkillEntity> findByIdUserId(UUID userId);

    void deleteByIdUserId(UUID userId);
}

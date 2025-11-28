package com.careerpath.infrastructure.persistence.jpa.repository;

import com.careerpath.infrastructure.persistence.jpa.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataProfileRepository extends JpaRepository<ProfileEntity, String> {
    Optional<ProfileEntity> findByUserId(String userId);
    boolean existsByUserId(String userId);
}

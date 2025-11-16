package com.careerpath.infrastructure.persistence.jpa.repository;

import com.careerpath.infrastructure.persistence.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataUserRepository extends JpaRepository<UserEntity, UUID> {
    boolean existsByEmail(String email);

    UserEntity findByEmail(String email);
}

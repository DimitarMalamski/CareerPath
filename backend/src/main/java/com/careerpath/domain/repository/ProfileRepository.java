package com.careerpath.domain.repository;

import com.careerpath.domain.modelOld.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {
    boolean existsById(java.util.UUID userId);
}

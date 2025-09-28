package com.careerpath.repository;

import com.careerpath.model.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ExperienceRepository extends JpaRepository<Experience, UUID> {}

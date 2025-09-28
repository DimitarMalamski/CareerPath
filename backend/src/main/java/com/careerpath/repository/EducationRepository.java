package com.careerpath.repository;

import com.careerpath.model.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface EducationRepository extends JpaRepository<Education, UUID> {}

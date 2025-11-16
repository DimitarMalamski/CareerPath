package com.careerpath.domain.repository;

import com.careerpath.domain.modelOld.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface EducationRepository extends JpaRepository<Education, UUID> {}

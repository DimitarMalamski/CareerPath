package com.careerpath.domain.repository;

import com.careerpath.domain.modelOld.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {}

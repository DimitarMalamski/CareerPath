package com.careerpath.infrastructure.persistence.jpa.repository;

import com.careerpath.infrastructure.persistence.jpa.entity.JobListingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SpringDataJobListingRepository
        extends JpaRepository<JobListingEntity, UUID> {
}
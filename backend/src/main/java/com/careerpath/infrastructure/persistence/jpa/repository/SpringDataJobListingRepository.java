package com.careerpath.infrastructure.persistence.jpa.repository;

import com.careerpath.infrastructure.persistence.jpa.entity.JobListingEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataJobListingRepository
        extends JpaRepository<JobListingEntity, UUID> {

    @Override
    @EntityGraph(attributePaths = {"jobSkills", "jobSkills.skill"})
    List<JobListingEntity> findAll();

    @Override
    @EntityGraph(attributePaths = {"jobSkills", "jobSkills.skill"})
    Optional<JobListingEntity> findById(UUID id);
}
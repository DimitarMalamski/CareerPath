package com.careerpath.infrastructure.persistence.jpa.repository;

import com.careerpath.infrastructure.persistence.jpa.entity.JobListingEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

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

    @Query("""
    SELECT j.id FROM JobListingEntity j
    JOIN j.jobSkills js
    JOIN js.skill s
    WHERE j.id <> :jobId
      AND s IN (
          SELECT s2 FROM JobListingEntity j2
          JOIN j2.jobSkills js2
          JOIN js2.skill s2
          WHERE j2.id = :jobId
      )
    GROUP BY j.id
    ORDER BY COUNT(s) DESC
""")
    List<UUID> findRelatedJobIds(@Param("jobId") UUID jobId, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"jobSkills", "jobSkills.skill"})
    List<JobListingEntity> findAllById(Iterable<UUID> ids);
}
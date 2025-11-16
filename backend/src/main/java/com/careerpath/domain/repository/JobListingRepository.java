package com.careerpath.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface JobListingRepository extends JpaRepository<JobListing, UUID> {}

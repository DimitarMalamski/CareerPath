package com.careerpath.repository;

import com.careerpath.model.JobListing;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface JobListingRepository extends JpaRepository<JobListing, UUID> {}

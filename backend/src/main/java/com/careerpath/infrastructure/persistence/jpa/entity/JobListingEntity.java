package com.careerpath.infrastructure.persistence.jpa.entity;

import com.careerpath.domain.model.enums.JobStatus;
import com.careerpath.domain.model.enums.JobType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "job_listings")
public class JobListingEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "recruiter_id", nullable = false)
    private UUID recruiterId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String company;

    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobType type;

    private String stackSummary;

    @Column(columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status;

    private LocalDate expiresAt;

    @Column(updatable = false)
    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime deletedAt;
}


package com.careerpath.model;

import com.careerpath.model.enums.JobStatus;
import com.careerpath.model.enums.JobType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "job_listings")
public class JobListing {
    @Id @GeneratedValue private UUID id;

    @ManyToOne @JoinColumn(name = "recruiter_id", nullable = false)
    private User recruiter;

    @Column(nullable = false) private String title;
    @Column(nullable = false) private String company;
    private String location;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private JobType type;

    private String stackSummary;

    @Column(columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private JobStatus status;

    private LocalDate expiresAt;

    @CreationTimestamp private OffsetDateTime createdAt;
    @UpdateTimestamp private OffsetDateTime updatedAt;
    private OffsetDateTime deletedAt;
}

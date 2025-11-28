package com.careerpath.domain.model;

import com.careerpath.domain.model.enums.JobStatus;
import com.careerpath.domain.model.enums.JobType;
import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobListing {

    private UUID id;
    private String recruiterId;

    private String title;
    private String company;
    private String location;

    private JobType type;
    private String stackSummary;
    private String description;

    private JobStatus status;
    private LocalDate expiresAt;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime deletedAt;

    private List<Skill> skills;
}

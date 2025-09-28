package com.careerpath.model;

import com.careerpath.model.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "application_status_history")
public class ApplicationStatusHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

    @ManyToOne @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Enumerated(EnumType.STRING) private ApplicationStatus oldStatus;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private ApplicationStatus newStatus;

    @ManyToOne @JoinColumn(name = "changed_by")
    private User changedBy;

    @Column(nullable = false) private OffsetDateTime changedAt;
}

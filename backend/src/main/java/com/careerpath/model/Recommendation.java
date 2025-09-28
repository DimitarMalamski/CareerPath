package com.careerpath.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "recommendations")
public class Recommendation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

    @ManyToOne @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne @JoinColumn(name = "job_id", nullable = false)
    private JobListing job;

    @Column(precision = 6, scale = 3, nullable = false)
    private BigDecimal score;

    private String modelVersion;
    @Column(nullable = false) private OffsetDateTime generatedAt;
}

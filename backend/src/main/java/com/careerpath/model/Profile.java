package com.careerpath.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "profiles")
public class Profile {
    @Id private UUID userId;

    @MapsId
    @OneToOne @JoinColumn(name = "user_id")
    private User user;

    private String firstName;
    private String lastName;
    private String headline;
    private String location;

    @Column(columnDefinition = "text")
    private String about;

    private String cvUrl;

    @Builder.Default
    @Column(nullable = false)
    private Boolean aiOptIn = true;

    @CreationTimestamp private OffsetDateTime createdAt;
    @UpdateTimestamp private OffsetDateTime updatedAt;
}
//package com.careerpath.domain.modelOld;
//
//import com.careerpath.domain.modelOld.enums.ApplicationStatus;
//import jakarta.persistence.*;
//import lombok.*;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;
//
//import java.time.OffsetDateTime;
//import java.util.UUID;
//
//@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
//@Entity
//@Table(name = "applications",
//    uniqueConstraints = @UniqueConstraint(columnNames = { "job_id", "user_id" }))
//public class Application {
//    @Id @GeneratedValue private UUID id;
//
//    @ManyToOne @JoinColumn(name = "job_id", nullable = false)
//    private JobListing job;
//
//    @ManyToOne @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    private String cvUrl;
//
//    @Column(columnDefinition = "text")
//    private String coverLetter;
//
//    @Enumerated(EnumType.STRING) @Column(nullable = false)
//    private ApplicationStatus status;
//
//    @CreationTimestamp private OffsetDateTime createdAt;
//    @UpdateTimestamp private OffsetDateTime updatedAt;
//}

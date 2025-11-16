//package com.careerpath.domain.modelOld;
//
//import jakarta.persistence.*;
//import lombok.*;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;
//
//import java.time.LocalDate;
//import java.time.OffsetDateTime;
//import java.util.UUID;
//
//@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
//@Entity @Table(name = "education")
//public class Education {
//    @Id @GeneratedValue private UUID id;
//
//    @ManyToOne @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    private String institution;
//    private String degree;
//    private String fieldOfStudy;
//    private LocalDate startDate;
//    private LocalDate endDate;
//    private String grade;
//
//    @Column(columnDefinition = "text")
//    private String description;
//
//    @CreationTimestamp private OffsetDateTime createdAt;
//    @UpdateTimestamp private OffsetDateTime updatedAt;
//}

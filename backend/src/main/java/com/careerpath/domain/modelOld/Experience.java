//package com.careerpath.domain.modelOld;
//
//import com.careerpath.domain.modelOld.enums.EmploymentType;
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
//@Entity @Table(name = "experience")
//public class Experience {
//    @Id @GeneratedValue private UUID id;
//
//    @ManyToOne @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    private String company;
//    private String title;
//
//    @Enumerated(EnumType.STRING) @Column(nullable = false)
//    private EmploymentType employmentType;
//
//    private String location;
//    private LocalDate startDate;
//    private LocalDate endDate;
//
//    @Builder.Default
//    @Column(nullable = false)
//    private Boolean isCurrent = false;
//
//    @Column(columnDefinition = "text")
//    private String description;
//
//    @CreationTimestamp private OffsetDateTime createdAt;
//    @UpdateTimestamp private OffsetDateTime updatedAt;
//}

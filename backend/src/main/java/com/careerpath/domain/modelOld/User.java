//package com.careerpath.domain.modelOld;
//
//import com.careerpath.domain.modelOld.enums.UserRole;
//import jakarta.persistence.*;
//import lombok.*;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;
//
//import java.time.OffsetDateTime;
//import java.util.UUID;
//
//@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
//@Entity @Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
//public class User {
//    @Id @GeneratedValue private UUID id;
//
//    @Column(nullable = false, unique = true) private String email;
//    @Column(name = "password_hash", nullable = false) private String passwordHash;
//
//    @Enumerated(EnumType.STRING) @Column(nullable = false)
//    private UserRole role;
//
//    private OffsetDateTime emailVerifiedAt;
//    @CreationTimestamp private OffsetDateTime createdAt;
//    @UpdateTimestamp private OffsetDateTime updatedAt;
//
//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, optional = true)
//    private Profile profile;
//}
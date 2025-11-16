//package com.careerpath.domain.modelOld;
//
//import com.careerpath.domain.modelOld.ids.BookmarkId;
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.time.OffsetDateTime;
//
//@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
//@Entity @Table(name = "bookmarks")
//public class Bookmark {
//    @EmbeddedId private BookmarkId id;
//
//    @ManyToOne @MapsId("userId") @JoinColumn(name = "user_id")
//    private User user;
//
//    @ManyToOne @MapsId("jobId") @JoinColumn(name = "job_id")
//    private JobListing job;
//
//    @Column(nullable = false) private OffsetDateTime createdAt;
//}

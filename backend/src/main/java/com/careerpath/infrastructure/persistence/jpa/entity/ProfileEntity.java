package com.careerpath.infrastructure.persistence.jpa.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "profiles")
public class ProfileEntity {

    @Id
    @Column(name = "user_id")
    private Long userId;

    private String fullName;
    private String headline;

    @Column(columnDefinition = "text")
    private String about;

    private String location;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<ProfileExperienceEmbeddable> experiences;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<ProfileSkillEmbeddable> skills;
}

package com.careerpath.infrastructure.persistence.jpa.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

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
    private String userId;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb", name = "data")
    private ProfileDataEmbeddable data;

    @Column(name = "ai_opt_in")
    private boolean aiOptIn;
}

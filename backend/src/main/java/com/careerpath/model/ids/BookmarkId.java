package com.careerpath.model.ids;

import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
@Embeddable
public class BookmarkId implements Serializable {
    private UUID userId;
    private UUID jobId;
}

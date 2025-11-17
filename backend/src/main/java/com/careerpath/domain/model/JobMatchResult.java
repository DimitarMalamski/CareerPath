package com.careerpath.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobMatchResult {
    private String jobListingId;
    private double score;
    private String explanation;
}

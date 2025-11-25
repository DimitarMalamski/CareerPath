package com.careerpath.domain.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobMatchResult {
    private String jobListingId;
    private double score;
    private String explanation;
    private String jobTitle;
    private String company;
    private String description;
    private String aiExplanation;
    private double finalScore;

    private List<String> matchedSkills;
    private List<String> missingSkills;
}

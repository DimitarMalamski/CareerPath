package com.careerpath.application.service;

import com.careerpath.domain.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobScoringService {

    public List<JobMatchResult> score(Profile profile, List<JobListing> listings) {
        return listings.stream()
                .map(job -> scoreSingle(profile, job))
                .toList();
    }

    JobMatchResult scoreSingle(Profile profile, JobListing job) {

        int score = 0;
        StringBuilder explanation = new StringBuilder();

        List<String> matchedSkills = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();

        for (ProfileSkill profileSkill : profile.getSkills()) {
            for (Skill jobSkill : job.getSkills()) {

                if (profileSkill.getName().equalsIgnoreCase(jobSkill.getName())) {
                    score += 20;
                    explanation.append("Matched skill: ")
                            .append(profileSkill.getName()).append(". ");
                    matchedSkills.add(jobSkill.getName());
                }
            }

            if (job.getStackSummary() != null &&
                    job.getStackSummary().toLowerCase().contains(profileSkill.getName().toLowerCase())) {
                score += 10;
                explanation.append("Stack summary contains ")
                        .append(profileSkill.getName()).append(". ");
            }
        }

        for (ProfileExperience exp : profile.getExperiences()) {

            if (job.getTitle() != null &&
                    exp.getTitle() != null &&
                    job.getTitle().toLowerCase().contains(exp.getTitle().toLowerCase())) {

                score += 10;
                explanation.append("Experience title matches job title. ");
            }

            if (exp.getDescription() != null) {
                for (Skill jobSkill : job.getSkills()) {
                    if (exp.getDescription().toLowerCase().contains(jobSkill.getName().toLowerCase())) {
                        score += 10;
                        explanation.append("Experience includes ")
                                .append(jobSkill.getName()).append(". ");
                    }
                }
            }
        }

        for (Skill jobSkill : job.getSkills()) {
            if (!matchedSkills.contains(jobSkill.getName())) {
                missingSkills.add(jobSkill.getName());
            }
        }

        if (profile.getLocation() != null &&
                job.getLocation() != null &&
                job.getLocation().toLowerCase().contains(profile.getLocation().toLowerCase())) {

            score += 5;
            explanation.append("Location matches. ");
        }

        double normalized = Math.min(score / 100.0, 1.0);

        return JobMatchResult.builder()
                .jobListingId(job.getId().toString())
                .score(normalized)
                .finalScore(normalized)
                .explanation(explanation.toString())
                .jobTitle(job.getTitle())
                .company(job.getCompany())
                .description(job.getDescription())
                .matchedSkills(matchedSkills)
                .missingSkills(missingSkills)
                .build();
    }
}

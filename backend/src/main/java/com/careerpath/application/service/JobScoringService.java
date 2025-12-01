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

        score += scoreSkillMatches(profile, job, explanation, matchedSkills);
        score += scoreExperienceMatches(profile, job, explanation);
        fillMissingSkills(job, matchedSkills, missingSkills);
        score += scoreLocation(profile, job, explanation);

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

    private int scoreSkillMatches(Profile profile, JobListing job,
                                  StringBuilder explanation, List<String> matchedSkills) {

        int score = 0;

        for (ProfileSkill profileSkill : profile.getSkills()) {

            for (Skill jobSkill : job.getSkills()) {
                if (profileSkill.getName().equalsIgnoreCase(jobSkill.getName())) {
                    score += 20;
                    explanation.append("Matched skill: ").append(profileSkill.getName()).append(". ");
                    matchedSkills.add(jobSkill.getName());
                }
            }

            if (containsIgnoreCase(job.getStackSummary(), profileSkill.getName())) {
                score += 10;
                explanation.append("Stack summary contains ")
                        .append(profileSkill.getName()).append(". ");
            }
        }
        return score;
    }

    private int scoreExperienceMatches(Profile profile, JobListing job,
                                       StringBuilder explanation) {

        int score = 0;

        for (ProfileExperience exp : profile.getExperiences()) {
            if (containsIgnoreCase(job.getTitle(), exp.getTitle())) {
                score += 10;
                explanation.append("Experience title matches job title. ");
            }
            if (exp.getDescription() != null) {
                for (Skill jobSkill : job.getSkills()) {
                    if (containsIgnoreCase(exp.getDescription(), jobSkill.getName())) {
                        score += 10;
                        explanation.append("Experience includes ")
                                .append(jobSkill.getName()).append(". ");
                    }
                }
            }
        }
        return score;
    }

    private void fillMissingSkills(JobListing job, List<String> matchedSkills, List<String> missingSkills) {
        for (Skill jobSkill : job.getSkills()) {
            if (!matchedSkills.contains(jobSkill.getName())) {
                missingSkills.add(jobSkill.getName());
            }
        }
    }

    private int scoreLocation(Profile profile, JobListing job, StringBuilder explanation) {
        if (containsIgnoreCase(job.getLocation(), profile.getLocation())) {
            explanation.append("Location matches. ");
            return 5;
        }
        return 0;
    }

    private boolean containsIgnoreCase(String text, String value) {
        return text != null && value != null && text.toLowerCase().contains(value.toLowerCase());
    }
}


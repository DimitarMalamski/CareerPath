package com.careerpath.application.service;

import com.careerpath.application.dto.JobMatchResultDto;
import com.careerpath.application.mapper.JobMatchResultMapper;
import com.careerpath.domain.model.*;
import com.careerpath.domain.port.AiJobMatcherPort;
import com.careerpath.domain.port.JobListingRepositoryPort;
import com.careerpath.domain.port.ProfileRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AiJobMatchingService {

    private final ProfileRepositoryPort profileRepository;
    private final JobListingRepositoryPort jobListingRepository;
    private final AiJobMatcherPort aiJobMatcherPort;
    private final JobMatchResultMapper jobMatchResultMapper;

    public List<JobMatchResultDto> getRecommendations(UUID userId) {

        Profile profile = profileRepository.getProfileByUserId(userId);
        List<JobListing> jobListings = jobListingRepository.findAll();

        List<JobMatchResult> baselineResults = jobListings.stream()
                .map(job -> scoreJob(profile, job))
                .toList();

        List<JobMatchResult> aiResults =
                aiJobMatcherPort.enhanceMatches(profile, baselineResults);

        return aiResults.stream()
                .map(jobMatchResultMapper::toDto)
                .toList();
    }

    private JobMatchResult scoreJob(Profile profile, JobListing job) {

        int score = 0;
        StringBuilder explanation = new StringBuilder();

        for (ProfileSkill profileSkill : profile.getSkills()) {
            for (Skill jobSkill : job.getSkills()) {
                if (profileSkill.getName().equalsIgnoreCase(jobSkill.getName())) {
                    score += 20;
                    explanation.append("Matched skill: ")
                            .append(profileSkill.getName()).append(". ");
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
                .explanation(explanation.toString())
                .jobTitle(job.getTitle())
                .company(job.getCompany())
                .description(job.getDescription())
                .build();
    }
}


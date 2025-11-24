package com.careerpath.application.mapper;

import com.careerpath.application.dto.JobRecommendationDto;
import com.careerpath.domain.model.JobListing;
import com.careerpath.domain.model.JobMatchResult;
import com.careerpath.domain.model.Skill;
import org.springframework.stereotype.Component;

@Component
public class JobRecommendationMapper {
    public JobRecommendationDto toDto(JobMatchResult jobMatchResult, JobListing jobListing) {
        return new JobRecommendationDto(
                jobListing.getId().toString(),
                jobListing.getTitle(),
                jobListing.getCompany(),
                jobListing.getLocation(),
                jobListing.getType().name(),
                jobListing.getSkills()
                        .stream()
                        .map(Skill::getName)
                        .toList(),
                jobListing.getDescription(),
                jobMatchResult.getFinalScore(),
                jobMatchResult.getAiExplanation(),
                jobListing.getCreatedAt().toString(),
                jobMatchResult.getMatchedSkills(),
                jobMatchResult.getMissingSkills()
        );
    }
}

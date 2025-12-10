package com.careerpath.application.mapper;

import com.careerpath.application.dto.JobDetailsDto;
import com.careerpath.domain.model.JobListing;
import com.careerpath.domain.model.JobMatchResult;
import com.careerpath.domain.model.Skill;

public class JobDetailsMapper {

    private JobDetailsMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static JobDetailsDto toDto(JobListing job, JobMatchResult match) {

        return new JobDetailsDto(
                job.getId().toString(),
                job.getTitle(),
                job.getCompany(),
                job.getLocation(),
                job.getType().name(),
                job.getDescription(),
                job.getStackSummary(),
                match.getFinalScore(),
                match.getAiExplanation(),
                job.getCreatedAt() != null ? job.getCreatedAt().toString() : null,
                job.getUpdatedAt() != null ? job.getUpdatedAt().toString() : null,
                job.getExpiresAt() != null ? job.getExpiresAt().toString() : null,
                job.getSkills() != null
                        ? job.getSkills().stream().map(Skill::getName).toList()
                        : null,
                match.getMatchedSkills(),
                match.getMissingSkills(),
                null
        );
    }
}

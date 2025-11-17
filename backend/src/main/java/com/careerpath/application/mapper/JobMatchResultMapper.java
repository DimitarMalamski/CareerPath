package com.careerpath.application.mapper;

import com.careerpath.application.dto.JobMatchResultDto;
import com.careerpath.domain.model.JobMatchResult;
import org.springframework.stereotype.Component;

@Component
public class JobMatchResultMapper {
    public JobMatchResultDto toDto(JobMatchResult jobMatchResult) {
        return new JobMatchResultDto(
                jobMatchResult.getJobListingId(),
                jobMatchResult.getScore(),
                jobMatchResult.getExplanation()
        );
    }
}

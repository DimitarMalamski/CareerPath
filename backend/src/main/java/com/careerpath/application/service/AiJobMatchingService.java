package com.careerpath.application.service;

import com.careerpath.application.dto.JobMatchResultDto;
import com.careerpath.application.mapper.JobMatchResultMapper;
import com.careerpath.domain.model.JobListing;
import com.careerpath.domain.model.Profile;
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

        return aiJobMatcherPort.matchJobs(profile, jobListings)
                .stream()
                .map(jobMatchResultMapper::toDto)
                .toList();
    }
}

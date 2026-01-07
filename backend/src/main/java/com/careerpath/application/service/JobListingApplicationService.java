package com.careerpath.application.service;

import com.careerpath.application.dto.CreateJobListingDto;
import com.careerpath.application.dto.JobListingDto;
import com.careerpath.application.mapper.JobListingDtoMapper;
import com.careerpath.domain.port.JobListingRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobListingApplicationService {
    private final JobListingRepositoryPort jobListingRepository;

    public List<JobListingDto> getAllJobListings() {
        return jobListingRepository.findAll()
                .stream()
                .map(JobListingDtoMapper::toDto)
                .toList();
    }

    public JobListingDto getJobListingById(UUID id) {
        return JobListingDtoMapper.toDto(
                jobListingRepository.findById(id)
        );
    }

    public JobListingDto createJobListing(CreateJobListingDto dto) {
        var job = jobListingRepository.save(
                dto.title(),
                dto.company(),
                dto.location()
        );

        return JobListingDtoMapper.toDto(job);
    }
}

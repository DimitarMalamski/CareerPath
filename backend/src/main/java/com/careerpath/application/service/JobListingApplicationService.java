package com.careerpath.application.service;

import com.careerpath.application.dto.CreateJobListingDto;
import com.careerpath.application.dto.JobListingDto;
import com.careerpath.application.event.NewJobListingCreatedEvent;
import com.careerpath.application.mapper.JobListingDtoMapper;
import com.careerpath.domain.port.JobListingRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobListingApplicationService {
    private final JobListingRepositoryPort jobListingRepository;
    private final ApplicationEventPublisher eventPublisher;

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

        eventPublisher.publishEvent(
                new NewJobListingCreatedEvent(job.getId())
        );

        return JobListingDtoMapper.toDto(job);
    }
}

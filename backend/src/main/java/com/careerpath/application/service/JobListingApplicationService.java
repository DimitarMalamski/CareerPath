package com.careerpath.application.service;

import com.careerpath.application.dto.CreateJobListingDto;
import com.careerpath.application.dto.JobListingDto;
import com.careerpath.application.event.NewJobListingCreatedEvent;
import com.careerpath.application.mapper.JobListingDtoMapper;
import com.careerpath.domain.model.JobListing;
import com.careerpath.domain.model.Skill;
import com.careerpath.domain.model.enums.JobType;
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
        // Convert skills from DTO → domain
        List<Skill> skills =
                dto.skills() == null
                        ? List.of()
                        : dto.skills().stream()
                        .map(name -> Skill.builder().name(name).build())
                        .toList();

        JobType type;
        try {
            type = dto.type() == null
                    ? JobType.FULL_TIME
                    : JobType.valueOf(dto.type().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Invalid job type: " + dto.type());
        }

        String description =
                dto.description() == null ? "" : dto.description();

        JobListing job = jobListingRepository.save(
                dto.title(),
                dto.company(),
                dto.location(),
                type,
                skills,
                description
        );

        JobListingDto jobDto = JobListingDtoMapper.toDto(job);

        eventPublisher.publishEvent(
                new NewJobListingCreatedEvent(jobDto)
        );

        return jobDto;
    }
}

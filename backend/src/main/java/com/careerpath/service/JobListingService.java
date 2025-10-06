package com.careerpath.service;

import com.careerpath.dto.JobListingDto;
import com.careerpath.mapper.JobListingMapper;
import com.careerpath.model.JobListing;
import com.careerpath.repository.JobListingRepository;
import com.careerpath.repository.JobSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobListingService {
    private final JobListingRepository jobListingRepository;
    private final JobSkillRepository jobSkillRepository;

    public List<JobListingDto> getAll() {
        List<JobListing> jobListings = jobListingRepository.findAll();

        return jobListings.stream()
                .map(job -> JobListingMapper.toDto(
                        job,
                        jobSkillRepository.findByJob(job)
                ))
                .toList();
    }
}

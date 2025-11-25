package com.careerpath.infrastructure.persistence.jpa.adapter;

import com.careerpath.domain.model.JobListing;
import com.careerpath.domain.model.Skill;
import com.careerpath.domain.port.JobListingRepositoryPort;
import com.careerpath.infrastructure.persistence.jpa.entity.JobListingEntity;
import com.careerpath.infrastructure.persistence.jpa.entity.JobSkillEntity;
import com.careerpath.infrastructure.persistence.jpa.mapper.JobListingEntityMapper;
import com.careerpath.infrastructure.persistence.jpa.mapper.SkillEntityMapper;
import com.careerpath.infrastructure.persistence.jpa.repository.SpringDataJobListingRepository;
import com.careerpath.infrastructure.persistence.jpa.repository.SpringDataJobSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JpaJobListingRepositoryAdapter implements JobListingRepositoryPort {

    private final SpringDataJobListingRepository jobListingRepository;
    private final SpringDataJobSkillRepository jobSkillRepository;

    @Override
    public List<JobListing> findAll() {
        List<JobListingEntity> jobListingEntities = jobListingRepository.findAll();

        return jobListingEntities.stream()
                .map(this::mapToDomainWithSkills)
                .toList();
    }

    @Override
    public JobListing findById(UUID id) {
        JobListingEntity jobListingEntity = jobListingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job listing now found"));

        return mapToDomainWithSkills(jobListingEntity);
    }

    private JobListing mapToDomainWithSkills(JobListingEntity entity) {
        JobListing jobListing = JobListingEntityMapper.toDomain(entity);

        List<JobSkillEntity> jobSkillEntities = jobSkillRepository.findByJob(entity);

        List<Skill> domainSkills = jobSkillEntities.stream()
                .map(js -> SkillEntityMapper.toDomain(js.getSkill()))
                .toList();

        jobListing.setSkills(domainSkills);

        return jobListing;
    }
}

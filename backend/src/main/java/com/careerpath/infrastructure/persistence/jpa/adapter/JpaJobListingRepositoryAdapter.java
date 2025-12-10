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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
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

    @Override
    public List<JobListing> findRelatedJobs(UUID jobId, int limit) {
        List<UUID> relatedIds =
                jobListingRepository.findRelatedJobIds(jobId, PageRequest.of(0, limit));

        List<JobListingEntity> entities =
                jobListingRepository.findAllById(relatedIds);

        List<JobListingEntity> ordered = relatedIds.stream()
                .map(id -> entities.stream()
                        .filter(e -> e.getId().equals(id))
                        .findFirst()
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();

        return ordered.stream()
                .map(this::mapToDomainWithSkills)
                .toList();
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

package com.careerpath.infrastructure.persistence.jpa.adapter;

import com.careerpath.domain.model.JobListing;
import com.careerpath.domain.model.JobSkill;
import com.careerpath.domain.port.JobSkillRepositoryPort;
import com.careerpath.infrastructure.persistence.jpa.entity.JobListingEntity;
import com.careerpath.infrastructure.persistence.jpa.mapper.JobSkillEntityMapper;
import com.careerpath.infrastructure.persistence.jpa.repository.SpringDataJobSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JpaJobSkillRepositoryAdapter implements JobSkillRepositoryPort {
    private final SpringDataJobSkillRepository jobSkillRepository;

    @Override
    public List<JobSkill> findByJobId(JobListing job) {
        JobListingEntity fakeEntity = new JobListingEntity();
        fakeEntity.setId(job.getId());

        return jobSkillRepository.findByJob(fakeEntity).stream()
                .map(JobSkillEntityMapper::toDomain)
                .toList();
    }
}

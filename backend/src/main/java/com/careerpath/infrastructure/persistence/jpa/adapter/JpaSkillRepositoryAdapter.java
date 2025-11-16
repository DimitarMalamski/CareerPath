package com.careerpath.infrastructure.persistence.jpa.adapter;

import com.careerpath.domain.model.Skill;
import com.careerpath.domain.port.SkillRepositoryPort;
import com.careerpath.infrastructure.persistence.jpa.mapper.SkillEntityMapper;
import com.careerpath.infrastructure.persistence.jpa.repository.SpringDataSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JpaSkillRepositoryAdapter implements SkillRepositoryPort {
    private final SpringDataSkillRepository skillRepository;

    @Override
    public Optional<Skill> findById(Integer id) {
        return skillRepository.findById(id)
                .map(SkillEntityMapper::toDomain);
    }

    @Override
    public List<Skill> findAll() {
        return skillRepository.findAll().stream()
                .map(SkillEntityMapper::toDomain)
                .toList();
    }
}

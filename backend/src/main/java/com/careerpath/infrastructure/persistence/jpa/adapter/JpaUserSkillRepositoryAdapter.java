package com.careerpath.infrastructure.persistence.jpa.adapter;

import com.careerpath.domain.model.UserSkill;
import com.careerpath.domain.port.UserSkillRepositoryPort;
import com.careerpath.infrastructure.persistence.jpa.entity.SkillEntity;
import com.careerpath.infrastructure.persistence.jpa.entity.UserSkillEntity;
import com.careerpath.infrastructure.persistence.jpa.mapper.UserSkillEntityMapper;
import com.careerpath.infrastructure.persistence.jpa.repository.SpringDataSkillRepository;
import com.careerpath.infrastructure.persistence.jpa.repository.SpringDataUserSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JpaUserSkillRepositoryAdapter implements UserSkillRepositoryPort {

    private final SpringDataUserSkillRepository userSkillRepository;
    private final SpringDataSkillRepository skillRepository;

    @Override
    public List<UserSkill> findByUserId(UUID userId) {
        return userSkillRepository.findByIdUserId(userId)
                .stream()
                .map(entity -> {
                    String skillName = skillRepository.findById(entity.getId().getSkillId())
                            .map(SkillEntity::getName)
                            .orElse(null);

                    return UserSkillEntityMapper.toDomain(entity, skillName);
                })
                .toList();
    }

    @Override
    public void saveAll(UUID userId, List<UserSkill> skills) {
        List<UserSkillEntity> entities = skills.stream()
                .map(skill -> {
                    UserSkillEntity entity = UserSkillEntityMapper.toEntity(skill);
                    entity.getId().setUserId(userId);
                    return entity;
                })
                .toList();

        userSkillRepository.saveAll(entities);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        userSkillRepository.deleteByIdUserId(userId);
    }
}

package com.careerpath.infrastructure.persistence.jpa.adapter;

import com.careerpath.domain.model.User;
import com.careerpath.domain.port.UserRepositoryPort;
import com.careerpath.infrastructure.persistence.jpa.entity.UserEntity;
import com.careerpath.infrastructure.persistence.jpa.mapper.UserEntityMapper;
import com.careerpath.infrastructure.persistence.jpa.repository.SpringDataUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JpaUserRepositoryAdapter implements UserRepositoryPort {

    private final SpringDataUserRepository springDataUserRepository;

    @Override
    public User findById(UUID id) {
        return springDataUserRepository.findById(id)
                .map(entity -> UserEntityMapper.toDomain(entity, null))
                .orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        UserEntity entity = springDataUserRepository.findByEmail(email);
        if (entity == null) return null;

        return UserEntityMapper.toDomain(entity, null);
    }

    @Override
    public User save(User user) {
        UserEntity entity = UserEntityMapper.toEntity(user);

        if (user.getId() == null) {
            entity.setId(null);
        }

        UserEntity saved = springDataUserRepository.save(entity);

        return UserEntityMapper.toDomain(saved, user.getSkills());
    }
}

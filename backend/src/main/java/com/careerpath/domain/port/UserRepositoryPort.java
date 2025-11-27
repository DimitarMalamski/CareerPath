package com.careerpath.domain.port;

import com.careerpath.domain.model.User;

import java.util.UUID;

public interface UserRepositoryPort {

    User findById(UUID id);

    User findByEmail(String email);

    User save(User user);
}

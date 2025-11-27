package com.careerpath.application.service;

import com.careerpath.domain.model.User;
import com.careerpath.domain.model.enums.UserRole;
import com.careerpath.domain.port.UserRepositoryPort;
import com.careerpath.domain.port.UserSyncPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserSyncService implements UserSyncPort {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public void syncUserFromExternal(String externalId, String email, boolean emailVerified) {
        User existing = userRepositoryPort.findByEmail(email);

        if (existing != null) {
            boolean changed = false;

            if (emailVerified && existing.getEmailVerifiedAt() == null) {
                existing.setEmailVerifiedAt(OffsetDateTime.now());
                changed = true;
            }

            if (changed) {
                existing.setUpdatedAt(OffsetDateTime.now());
                userRepositoryPort.save(existing);
            }

            return;
        }

        User newUser = User.builder()
                .email(email)
                .passwordHash("supabase_managed")
                .role(UserRole.SEEKER)
                .emailVerifiedAt(emailVerified ? OffsetDateTime.now() : null)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        userRepositoryPort.save(newUser);
    }
}

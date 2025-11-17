package com.careerpath.domain.model;

import com.careerpath.domain.model.enums.SkillLevel;
import com.careerpath.domain.model.enums.UserRole;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class User {

    private UUID id;
    private String email;
    private String passwordHash;
    private UserRole role;

    private OffsetDateTime emailVerifiedAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    @Builder.Default
    private List<UserSkill> skills = new ArrayList<>();

    // Domain level operations (DDD)

    public void addSkill(UserSkill skill) {
        this.skills.add(skill);
    }

    public void removeSkillById(Integer skillId) {
        this.skills.removeIf(s -> s.getSkillId().equals(skillId));
    }

    public void updateSkillLevel(Integer skillId, SkillLevel newLevel) {
        this.skills.stream()
                .filter(s -> s.getSkillId().equals(skillId))
                .findFirst()
                .ifPresent(s -> s.setLevel(newLevel));
    }

    public boolean hasSkill(Integer skillId) {
        return this.skills.stream()
                .anyMatch(s -> s.getSkillId().equals(skillId));
    }
}

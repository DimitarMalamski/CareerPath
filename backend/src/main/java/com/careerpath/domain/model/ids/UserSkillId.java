package com.careerpath.domain.model.ids;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserSkillId {
    private UUID userId;
    private Integer skillId;
}

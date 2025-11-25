package com.careerpath.domain.model;

import com.careerpath.domain.model.enums.SkillLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSkill {

    private String skillName;
    private SkillLevel level;

    private java.util.UUID userId;
    private Integer skillId;
}

package com.careerpath.domain.model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobSkill {
    private UUID jobId;
    private Integer skillId;
}

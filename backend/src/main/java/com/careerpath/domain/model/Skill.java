package com.careerpath.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Skill {
    private Integer id;
    private String name;
}

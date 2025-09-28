package com.careerpath.repository;

import com.careerpath.model.UserSkill;
import com.careerpath.model.ids.UserSkillId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSkillRepository extends JpaRepository<UserSkill, UserSkillId> {}

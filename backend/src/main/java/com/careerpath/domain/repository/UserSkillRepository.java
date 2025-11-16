package com.careerpath.domain.repository;

import com.careerpath.domain.modelOld.UserSkill;
import com.careerpath.domain.modelOld.ids.UserSkillId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSkillRepository extends JpaRepository<UserSkill, UserSkillId> {}

package com.careerpath.domain.repository;

import com.careerpath.domain.modelOld.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<Application, UUID> {}

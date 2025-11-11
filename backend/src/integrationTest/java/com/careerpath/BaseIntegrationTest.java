package com.careerpath;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
public abstract class BaseIntegrationTest {

    @Container
    protected static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("careerpath_test")
                    .withUsername("cp_user")
                    .withPassword("cp_pass");

    @BeforeAll
    static void setUp() {
        boolean isCI = System.getenv("CI") != null;

        if (!isCI && System.getenv("SPRING_DATASOURCE_URL") == null) {
            postgres.start();

            System.setProperty("spring.datasource.url", postgres.getJdbcUrl());
            System.setProperty("spring.datasource.username", postgres.getUsername());
            System.setProperty("spring.datasource.password", postgres.getPassword());
            System.setProperty("spring.jpa.hibernate.ddl-auto", "create-drop");
        }
    }
}


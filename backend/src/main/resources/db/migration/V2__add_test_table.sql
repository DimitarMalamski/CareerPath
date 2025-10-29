--Simple Flyway test migration
CREATE TABLE IF NOT EXISTS flyway_test (
    id SERIAL PRIMARY KEY,
    message VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
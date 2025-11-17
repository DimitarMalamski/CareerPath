-- Drop old normalized profile tables
DROP TABLE IF EXISTS experience_skills CASCADE;
DROP TABLE IF EXISTS experience CASCADE;
DROP TABLE IF EXISTS education CASCADE;
DROP TABLE IF EXISTS user_skills CASCADE;
DROP TABLE IF EXISTS flyway_test CASCADE;

-- Only drop if no longer needed
-- DROP TABLE IF EXISTS skills CASCADE;

-- Drop old profile table
DROP TABLE IF EXISTS profiles CASCADE;
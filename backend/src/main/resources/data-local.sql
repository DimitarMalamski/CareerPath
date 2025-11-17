-- USERS ------------------------------------------------------

INSERT INTO users (id, email, password_hash, role, created_at)
SELECT gen_random_uuid(), 'anna@example.com', 'hashed123', 'SEEKER', now()
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'anna@example.com');

INSERT INTO users (id, email, password_hash, role, created_at)
SELECT gen_random_uuid(), 'recruiter@example.com', 'hashed456', 'RECRUITER', now()
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'recruiter@example.com');

INSERT INTO users (id, email, password_hash, role, created_at)
SELECT gen_random_uuid(), 'admin@example.com', 'hashed789', 'ADMIN', now()
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@example.com');


-- JOB LISTINGS ----------------------------------------------

INSERT INTO job_listings (id, recruiter_id, title, company, type, status, stack_summary, created_at)
SELECT
    gen_random_uuid(),
    (SELECT id FROM users WHERE role = 'RECRUITER' LIMIT 1),
    'Java Backend Developer',
    'Google',
    'FULL_TIME',
    'PUBLISHED',
    'Java, Spring Boot, PostgreSQL',
    now()
WHERE NOT EXISTS (
    SELECT 1 FROM job_listings WHERE title = 'Java Backend Developer' AND company = 'Google'
    );


-- SAMPLE JSONB PROFILE ---------------------------------------

INSERT INTO profiles (user_id, data, ai_opt_in)
SELECT
    (SELECT id FROM users WHERE email = 'anna@example.com'),
    '{
      "fullName": "Anna Petrescu",
      "headline": "Aspiring Java Developer",
      "about": "I love backend engineering.",
      "location": "Eindhoven, Netherlands",
      "skills": [
        {"name": "Java", "level": "INTERMEDIATE"},
        {"name": "Spring Boot", "level": "BEGINNER"}
      ],
      "experiences": [
        {
          "company": "SoftUni",
          "title": "Backend Student",
          "employmentType": "FULL_TIME",
          "startDate": "2023-01-01",
          "endDate": null,
          "current": true,
          "description": "Learned Java and Spring."
        }
      ]
    }'::jsonb,
    true
    WHERE NOT EXISTS (
    SELECT 1 FROM profiles WHERE user_id = (SELECT id FROM users WHERE email = 'anna@example.com')
);

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

INSERT INTO job_listings (id, recruiter_id, title, company, location, type, status, stack_summary, description, created_at)
SELECT
    gen_random_uuid(),
    (SELECT id FROM users WHERE role = 'RECRUITER' LIMIT 1),
    'Java Backend Developer',
    'Google',
    'Eindhoven',
    'FULL_TIME',
    'PUBLISHED',
    'Java, Spring Boot, PostgreSQL',
    'We are looking for a skilled Java Backend Developer to join our backend engineering team. You will work with Spring Boot, REST APIs, cloud services, and relational databases. Experience with distributed systems is a plus.',
    now()
WHERE NOT EXISTS (
    SELECT 1 FROM job_listings WHERE title = 'Java Backend Developer' AND company = 'Google'
    );

INSERT INTO job_listings (
    id,
    recruiter_id,
    title,
    company,
    location,
    type,
    status,
    stack_summary,
    description,
    created_at
)
SELECT
    gen_random_uuid(),
    (SELECT id FROM users WHERE role = 'RECRUITER' LIMIT 1),
    'React Frontend Developer',
    'Meta',
    'Dubai',
    'REMOTE',
    'PUBLISHED',
    'React, TypeScript, GraphQL',
    'This role involves building highly interactive UI components with React, optimizing performance, and collaborating with designers. Experience with TypeScript and GraphQL is required.',
    now()
WHERE NOT EXISTS (
    SELECT 1 FROM job_listings WHERE title = 'React Frontend Developer' AND company = 'Meta'
    );

INSERT INTO job_listings (
    id,
    recruiter_id,
    title,
    company,
    location,
    type,
    status,
    stack_summary,
    description,
    created_at
)
SELECT
    gen_random_uuid(),
    (SELECT id FROM users WHERE role = 'RECRUITER' LIMIT 1),
    'DevOps Engineer',
    'AWS',
    'Blagoevgrad',
    'HYBRID',
    'PUBLISHED',
    'AWS, Docker, CI/CD, Terraform',
    'Work with AWS cloud services, automate deployments, maintain CI/CD pipelines, and improve infrastructure reliability. Familiarity with containerization is essential.',
    now()
WHERE NOT EXISTS (
    SELECT 1 FROM job_listings WHERE title = 'DevOps Engineer' AND company = 'AWS'
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

-- JOB SKILLS: Java Backend Developer (Google)
INSERT INTO job_skills (job_id, skill_id)
SELECT jl.id, s.id
FROM job_listings jl, skills s
WHERE jl.title = 'Java Backend Developer'
  AND jl.company = 'Google'
  AND s.name IN ('Java', 'Spring Boot', 'PostgreSQL')
  AND NOT EXISTS (
    SELECT 1 FROM job_skills js
    WHERE js.job_id = jl.id AND js.skill_id = s.id
);

-- JOB SKILLS: React Frontend Developer (Meta)
INSERT INTO job_skills (job_id, skill_id)
SELECT jl.id, s.id
FROM job_listings jl, skills s
WHERE jl.title = 'React Frontend Developer'
  AND jl.company = 'Meta'
  AND s.name IN ('React', 'TypeScript', 'GraphQL')
  AND NOT EXISTS (
    SELECT 1 FROM job_skills js
    WHERE js.job_id = jl.id AND js.skill_id = s.id
);

-- JOB SKILLS: DevOps Engineer (AWS)
INSERT INTO job_skills (job_id, skill_id)
SELECT jl.id, s.id
FROM job_listings jl, skills s
WHERE jl.title = 'DevOps Engineer'
  AND jl.company = 'AWS'
  AND s.name IN ('AWS', 'Docker', 'CI/CD', 'Terraform')
  AND NOT EXISTS (
    SELECT 1 FROM job_skills js
    WHERE js.job_id = jl.id AND js.skill_id = s.id
);

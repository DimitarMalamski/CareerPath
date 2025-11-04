INSERT INTO job_listings (id, recruiter_id, title, company, type, status, created_at)
VALUES
    (gen_random_uuid(),
     (SELECT id FROM users WHERE role = 'RECRUITER' LIMIT 1),
    'Java Backend Developer', 'Google', 'FULL_TIME', 'PUBLISHED', now());
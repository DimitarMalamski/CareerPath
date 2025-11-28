-- SKILLS ------------------------------------------------------

INSERT INTO skills (name) VALUES
                              ('Java'),
                              ('Spring Boot'),
                              ('PostgreSQL'),
                              ('React'),
                              ('TypeScript'),
                              ('GraphQL'),
                              ('AWS'),
                              ('Docker'),
                              ('Terraform'),
                              ('CI/CD')
    ON CONFLICT DO NOTHING;


-- JOB LISTINGS -----------------------------------------------

INSERT INTO job_listings (id, recruiter_id, title, company, location, type, status, stack_summary, description, created_at)
VALUES (
           gen_random_uuid(),
           '11111111-2222-3333-4444-555555555555',
           'Java Backend Developer',
           'Google',
           'Eindhoven',
           'FULL_TIME',
           'PUBLISHED',
           'Java, Spring Boot, PostgreSQL',
           'We are looking for a skilled Java Backend Developer...',
           now()
       )
    ON CONFLICT DO NOTHING;

INSERT INTO job_listings (id, recruiter_id, title, company, location, type, status, stack_summary, description, created_at)
VALUES (
           gen_random_uuid(),
           '11111111-2222-3333-4444-555555555555',
           'React Frontend Developer',
           'Meta',
           'Dubai',
           'REMOTE',
           'PUBLISHED',
           'React, TypeScript, GraphQL',
           'This role involves building highly interactive UI components...',
           now()
       )
    ON CONFLICT DO NOTHING;

INSERT INTO job_listings (id, recruiter_id, title, company, location, type, status, stack_summary, description, created_at)
VALUES (
           gen_random_uuid(),
           '11111111-2222-3333-4444-555555555555',
           'DevOps Engineer',
           'AWS',
           'Blagoevgrad',
           'HYBRID',
           'PUBLISHED',
           'AWS, Docker, CI/CD, Terraform',
           'Work with AWS cloud services, automation...',
           now()
       )
    ON CONFLICT DO NOTHING;

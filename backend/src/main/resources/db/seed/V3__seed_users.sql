INSERT INTO users (id, email, password_hash, role, created_at)
VALUES
    (gen_random_uuid(), 'anna@example.com', 'hashed123', 'SEEKER', now()),
    (gen_random_uuid(), 'recruiter@example.com', 'hashed456', 'RECRUITER', now()),
    (gen_random_uuid(), 'admin@example.com', 'hashed789', 'ADMIN', now());
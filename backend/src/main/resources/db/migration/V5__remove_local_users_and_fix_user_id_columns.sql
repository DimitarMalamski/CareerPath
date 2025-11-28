ALTER TABLE job_listings
DROP CONSTRAINT IF EXISTS job_listings_recruiter_id_fkey;

ALTER TABLE profiles
DROP CONSTRAINT IF EXISTS profiles_user_id_fkey;

DROP TABLE IF EXISTS users CASCADE;

ALTER TABLE job_listings
ALTER COLUMN recruiter_id TYPE VARCHAR(255)
    USING recruiter_id::text;

ALTER TABLE profiles
ALTER COLUMN user_id TYPE VARCHAR(255)
    USING user_id::text;

CREATE INDEX IF NOT EXISTS idx_job_listings_recruiter_id
    ON job_listings(recruiter_id);

CREATE INDEX IF NOT EXISTS idx_profiles_user_id
    ON profiles(user_id);

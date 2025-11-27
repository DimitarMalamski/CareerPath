-- 1. Drop FK (users.id → profiles.user_id)
ALTER TABLE profiles
DROP CONSTRAINT IF EXISTS profiles_user_id_fkey;

-- 2. Convert UUID → VARCHAR(255)
ALTER TABLE profiles
ALTER COLUMN user_id TYPE VARCHAR(255)
    USING user_id::text;

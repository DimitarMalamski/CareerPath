-- USERS
CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('SEEKER','RECRUITER','ADMIN')),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- SKILLS
CREATE TABLE skills (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- JOB LISTINGS
CREATE TABLE job_listings (
      id UUID PRIMARY KEY,
      recruiter_id UUID NOT NULL REFERENCES users(id),
      title VARCHAR(255) NOT NULL,
      company VARCHAR(255) NOT NULL,
      location VARCHAR(255),
      type VARCHAR(20) NOT NULL CHECK (type IN ('INTERNSHIP','FULL_TIME','PART_TIME','CONTRACT')),
      stack_summary VARCHAR(255),
      description TEXT,
      status VARCHAR(20) NOT NULL CHECK (status IN ('DRAFT','PENDING','PUBLISHED','CLOSED')),
      expires_at DATE,
      created_at TIMESTAMPTZ DEFAULT NOW(),
      updated_at TIMESTAMPTZ DEFAULT NOW(),
      deleted_at TIMESTAMPTZ
);

-- JOB SKILLS
CREATE TABLE job_skills (
    job_id UUID REFERENCES job_listings(id) ON DELETE CASCADE,
    skill_id INT REFERENCES skills(id),
    PRIMARY KEY (job_id, skill_id)
);

-- PROFILES (JSONB)
CREATE TABLE profiles (
      user_id UUID PRIMARY KEY REFERENCES users(id),
      data JSONB NOT NULL,
      ai_opt_in BOOLEAN NOT NULL DEFAULT true,
      created_at TIMESTAMPTZ DEFAULT NOW(),
      updated_at TIMESTAMPTZ DEFAULT NOW()
);

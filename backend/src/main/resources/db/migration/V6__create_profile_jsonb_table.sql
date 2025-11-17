CREATE TABLE profiles (
      user_id UUID PRIMARY KEY REFERENCES users(id),
      data JSONB NOT NULL,
      ai_opt_in BOOLEAN NOT NULL DEFAULT true,
      created_at TIMESTAMPTZ DEFAULT NOW(),
      updated_at TIMESTAMPTZ DEFAULT NOW()
);
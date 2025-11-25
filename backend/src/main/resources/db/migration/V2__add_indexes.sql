-- Speed up JSONB lookups
CREATE INDEX idx_profiles_data_gin ON profiles USING gin (data);

-- Speed up job filtering
CREATE INDEX idx_job_listings_status ON job_listings(status);
CREATE INDEX idx_job_listings_type ON job_listings(type);
CREATE INDEX idx_job_listings_location ON job_listings(location);
export interface JobDetails {
  id: string;
  title: string;
  company: string;
  location: string;
  type: string;
  description: string;
  stackSummary: string | null;
  finalScore: number;
  aiExplanation: string;
  createdAt: string;
  updatedAt: string | null;
  expiresAt: string | null;
  skills: string[];
  matchedSkills: string[];
  missingSkills: string[];
  applyUrl: string | null;
}

export interface JobSummary {
  stackSummary?: string;
  finalScore: number;
  matchedSkills?: string[];
  missingSkills?: string[];
  applyUrl?: string | null;
}

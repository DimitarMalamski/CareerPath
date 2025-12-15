export interface JobSummary {
  stackSummary?: string | null;
  finalScore: number;
  matchedSkills?: string[] | null;
  missingSkills?: string[] | null;
  applyUrl?: string | null;
}

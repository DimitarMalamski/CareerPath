export interface JobRecommendation {
  id: string;
  title: string;
  company: string;
  location: string;
  type: string;
  skills: string[];
  description: string;
  finalScore: number;
  aiExplanation: string;
}

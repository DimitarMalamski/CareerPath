export interface ProfileExperience {
  id: number | null;
  company: string;
  title: string;
  employmentType: string;
  location: string;
  startDate: string | null;
  endDate: string | null;
  isCurrent: boolean;
  description: string;
}

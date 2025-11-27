import {ProfileSkill} from './profile-skill';
import {ProfileExperience} from './profile-experience';

export interface ProfileDto {
  fullName: string;
  headline: string;
  about: string;
  location: string;
  skills: ProfileSkill[];
  experiences: ProfileExperience[];
  aiOptIn: boolean;
}

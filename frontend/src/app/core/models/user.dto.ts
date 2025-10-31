import {ProfileDto} from './profile.dto';

export type UserRole = 'ADMIN' | 'RECRUITER' | 'CANDIDATE';

export interface UserDto {
  id: string;
  email: string;
  role: UserRole;
  profile?: ProfileDto | null;
}

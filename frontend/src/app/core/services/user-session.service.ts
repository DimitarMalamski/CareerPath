import {inject, Injectable} from '@angular/core';
import { SupabaseService } from './supabase.service';

@Injectable({ providedIn: 'root' })
export class UserSessionService {
  private readonly supabase = inject(SupabaseService);

  async getUser() {
    const session = await this.supabase.getSession();
    return session?.user ?? null;
  }

  async getUserId(): Promise<string | null> {
    return (await this.getUser())?.id ?? null;
  }

  async getEmail(): Promise<string | null> {
    return (await this.getUser())?.email ?? null;
  }

  async isLoggedIn(): Promise<boolean> {
    return !!(await this.getUser());
  }
}

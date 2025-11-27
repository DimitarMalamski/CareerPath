import {inject, Injectable} from '@angular/core';
import { SupabaseService } from './supabase.service';

@Injectable({ providedIn: 'root' })
export class UserIdentityService {
  private readonly supabase = inject(SupabaseService);

  async getUserId(): Promise<string | null> {
    const session = await this.supabase.getSession();
    return session?.user?.id ?? null;
  }

  async getEmail(): Promise<string | null> {
    const session = await this.supabase.getSession();
    return session?.user?.email ?? null;
  }
}


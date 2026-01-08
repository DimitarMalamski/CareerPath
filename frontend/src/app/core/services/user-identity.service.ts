import {inject, Injectable} from '@angular/core';
import { SupabaseService } from './supabase.service';
import { environment } from '../../../environments/environment'

@Injectable({ providedIn: 'root' })
export class UserIdentityService {
  private readonly supabase = inject(SupabaseService);

  async getUserId(): Promise<string | null> {
    if (environment.e2e) {
      return 'c004a123-226d-4c19-b44e-5b7251f09282'
    }

    const session = await this.supabase.getSession();
    return session?.user?.id ?? null;
  }

  async getEmail(): Promise<string | null> {
    const session = await this.supabase.getSession();
    return session?.user?.email ?? null;
  }
}


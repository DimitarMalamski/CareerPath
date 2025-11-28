import {inject, Injectable} from '@angular/core';
import { SupabaseService } from './supabase.service';

@Injectable({ providedIn: 'root' })
export class AuthInitService {
  private initialized = false;
  private readonly supabase = inject(SupabaseService);

  async init() {
    if (this.initialized) return;
    this.initialized = true;

    const session = await this.supabase.getSession();
    if (!session) return;

    localStorage.setItem("jwt_token", session.access_token);
  }
}

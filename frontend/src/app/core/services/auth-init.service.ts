import { Injectable } from '@angular/core';
import { SupabaseService } from './supabase.service';

@Injectable({ providedIn: 'root' })
export class AuthInitService {
  private initialized = false;

  constructor(private readonly supabase: SupabaseService) {}

  async init() {
    if (this.initialized) return;
    this.initialized = true;

    const session = await this.supabase.getSession();
    if (!session) return;

    localStorage.setItem("authToken", session.access_token);
  }
}

import { Injectable } from '@angular/core';
import { SupabaseService } from './supabase.service';
import {environment} from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthInitService {
  private initialized = false;

  constructor(private readonly supabase: SupabaseService) {}

  async init() {
    if (this.initialized) return;
    this.initialized = true;

    const session = await this.supabase.getSession();
    if (!session || !session.user) return;

    const user = session.user;

    const response = await fetch(`${environment.apiUrl}/auth/sync`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        externalId: user.id,
        email: user.email,
        emailVerified: !!user.email_confirmed_at,
      })
    });

    const result = await response.json();

    if (result.token) {
      localStorage.setItem('jwt_token', result.token);
      console.log("Backend JWT stored:", result.token);
    }
  }
}

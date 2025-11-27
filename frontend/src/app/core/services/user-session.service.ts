import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class UserSessionService {
  private readonly tokenKey = 'jwt_token';

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  private decode(): any | null {
    const token = this.getToken();
    if (!token) return null;

    try {
      const payload = token.split('.')[1];
      return JSON.parse(atob(payload));
    } catch (e) {
      console.error('Invalid JWT token', e);
      return null;
    }
  }

  getUserId(): string | null {
    return this.decode()?.sub ?? null;
  }

  getEmail(): string | null {
    return this.decode()?.email ?? null;
  }

  getRole(): string | null {
    return this.decode()?.role ?? null;
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }
}

import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SupabaseService } from '../../../core/services/supabase.service';
import {Router, RouterModule} from '@angular/router';
import {environment} from '../../../../environments/environment';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  errorMessage: string | null = null;

  constructor(
    private readonly supabase: SupabaseService,
    private readonly router: Router
  ) {}

  async login() {
    this.errorMessage = null;

    const { data, error } = await this.supabase.signIn(this.email, this.password);

    if (error) {
      this.errorMessage = error.message;
      return;
    }

    const user = data.user;
    if (!user) {
      this.errorMessage = "Unexpected error: No Supabase user returned.";
      return;
    }

    const syncResponse = await fetch(`${environment.apiUrl}/auth/sync`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        externalId: user.id,
        email: user.email,
        emailVerified: !!user.email_confirmed_at
      })
    });

    const syncData = await syncResponse.json();

    localStorage.setItem("jwt_token", syncData.token);

    await this.router.navigate(['/home']);
  }

  async loginWithGoogle() {
    await this.supabase.getClient().auth.signInWithOAuth({
      provider: 'google',
      options: {
        redirectTo: `${globalThis.location.origin}`
      }
    });
  }
}

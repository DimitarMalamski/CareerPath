import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SupabaseService } from '../../../core/services/supabase.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
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
    const { error } = await this.supabase.signIn(this.email, this.password);

    if (error) {
      this.errorMessage = error.message;
      return;
    }

    await this.router.navigate(['/home']);
  }

  async loginWithGoogle() {
    await this.supabase.client.auth.signInWithOAuth({
      provider: 'google',
      options: {
        redirectTo: globalThis.location.origin
      }
    });
  }
}

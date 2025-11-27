import {Component, inject} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SupabaseService } from '../../../core/services/supabase.service';
import {Router, RouterModule} from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  email = '';
  password= '';
  errorMessage: string | null = null;

  private readonly supabase = inject(SupabaseService);
  private readonly router = inject(Router);

  async login() {
    this.errorMessage = null;

    const { data, error } = await this.supabase.signIn(this.email, this.password);

    if (error) {
      this.errorMessage = error.message;
      return;
    }

    const session = data.session;
    if (!session) {
      this.errorMessage = "Unexpected error: no session returned.";
      return;
    }

    localStorage.setItem("jwt_token", session.access_token);

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

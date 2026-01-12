import {Component, inject} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SupabaseService } from '../../../core/services/supabase.service';
import {RouterModule} from '@angular/router';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './forgot-password.component.html'
})
export class ForgotPasswordComponent {
  email = '';
  message: string | null = null;
  error: string | null = null;

  private readonly supabase = inject(SupabaseService);

  async sendResetEmail() {
    this.message = null;
    this.error = null;

    const { error } = await this.supabase.getClient().auth.resetPasswordForEmail(
      this.email,
      {
        redirectTo: 'https://careerpath-ip.com/auth/reset-password'
      }
    );

    if (error) {
      this.error = error.message;
      return;
    }

    this.message = `A reset link has been sent to ${this.email}`;
  }
}

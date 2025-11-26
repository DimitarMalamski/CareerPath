import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { SupabaseService } from '../../../core/services/supabase.service';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reset-password.component.html'
})
export class ResetPasswordComponent {
  password = '';
  confirmPassword = '';
  error: string | null = null;

  constructor(
    private readonly supabase: SupabaseService,
    private readonly router: Router
  ) {}

  async updatePassword() {
    this.error = null;

    if (this.password !== this.confirmPassword) {
      this.error = 'Passwords do not match.';
      return;
    }

    const { error } = await this.supabase.getClient().auth.updateUser({
      password: this.password
    });

    if (error) {
      this.error = error.message;
      return;
    }

    await this.router.navigate(['/login']);
  }
}

import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {Router, RouterModule} from '@angular/router';
import { SupabaseService } from '../../../core/services/supabase.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './register.component.html'
})
export class RegisterComponent {
  email = '';
  password = '';
  confirmPassword = '';
  errorMessage: string | null = null;

  constructor(
    private readonly supabase: SupabaseService,
    private readonly router: Router
  ) {}

  async register() {
    this.errorMessage = null;

    if (this.password !== this.confirmPassword) {
      this.errorMessage = "Passwords do not match.";
      return;
    }

    const { data, error } = await this.supabase.getClient().auth.signUp({
      email: this.email,
      password: this.password,
    });

    if (error) {
      this.errorMessage = error.message;
      return;
    }

    this.errorMessage = null;

    await this.router.navigate(['/check-email']);
  }
}

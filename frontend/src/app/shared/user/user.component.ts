import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from '../../core/services/user.service';
import { Observable } from 'rxjs';
import { UserDto } from '../../core/models/user.dto';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user.component.html'
})
export class UserComponent implements OnInit {
  private userService = inject(UserService);

  users$!: Observable<UserDto[]>;
  loading = true;
  error: string | null = null;

  ngOnInit(): void {
    this.users$ = this.userService.getUsers();
    this.users$.subscribe({
      next: () => (this.loading = false),
      error: (err) => { this.loading = false; this.error = err?.message ?? 'Failed to load users'; }
    });
  }

  trackById = (_: number, u: UserDto) => u.id;

  displayName(u: UserDto): string {
    const p = u.profile;
    const full = [p?.firstname, p?.lastname].filter(Boolean).join(' ').trim();
    return full || u.email.split('@')[0];
  }
}

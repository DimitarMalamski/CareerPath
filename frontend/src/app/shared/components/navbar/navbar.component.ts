import {Component, inject, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {Router, RouterModule} from '@angular/router';
import {SupabaseService} from '../../../core/services/supabase.service';
import { Session } from '@supabase/supabase-js';
import { JobWebsocketService } from '../../../core/services/job-websocket.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html'
})

export class NavbarComponent implements OnInit {
  menuOpen = false;
  session: Session | null = null;

  private readonly supabase = inject(SupabaseService);
  private readonly router = inject(Router);
  private readonly jobWs = inject(JobWebsocketService);

  readonly hasNewJobs$ = this.jobWs.newJob$;

  ngOnInit(): void {
    this.loadSession();

    this.supabase.getClient().auth.onAuthStateChange((_event, session) => {
      this.session = session;
    });
  }

  private async loadSession(): Promise<void> {
    this.session = await this.supabase.getSession();
  }

  toggleMenu(): void {
    this.menuOpen = !this.menuOpen;
  }

  async logout(): Promise<void> {
    await this.supabase.signOut();
    await this.router.navigate(['/home']);
  }

  onJobsClick(): void {
    this.jobWs.clearNewJobIndicator();
    this.menuOpen = false;
  }
}

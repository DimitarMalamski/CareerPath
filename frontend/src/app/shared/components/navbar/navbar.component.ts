import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {Router, RouterModule} from '@angular/router';
import {SupabaseService} from '../../../core/services/supabase.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html'
})

export class NavbarComponent implements OnInit {
  menuOpen = false;
  session: any = null;

  constructor(
    private readonly supabase: SupabaseService,
    private readonly router: Router
  ) {}

  async ngOnInit() {
    this.session = await this.supabase.getSession();

    this.supabase.getClient().auth.onAuthStateChange((_event, session) => {
      this.session = session;
    });
  }

  toggleMenu() {
    this.menuOpen = !this.menuOpen;
  }

  async logout() {
    await this.supabase.signOut();
    await this.router.navigate(['/home']);
  }
}

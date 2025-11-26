import { Routes } from '@angular/router';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { HomeComponent } from './features/home/home.component';
import { UserComponent } from './shared/user/user.component';
import { JobsListComponent } from './features/jobs/jobs-list/jobs-list.component';
import {AuthLayoutComponent} from './layout/auth-layout/auth-layout.component';
import {LoginComponent} from './features/auth/login/login.component';
import {RegisterComponent} from './features/auth/register/register.component';

export const routes: Routes = [
  // Redirect for clean URLs
  { path: 'login', redirectTo: 'auth/login', pathMatch: 'full' },
  { path: 'register', redirectTo: 'auth/register', pathMatch: 'full' },

  // AUTH LAYOUT (no navbar/footer)
  {
    path: 'auth',
    component: AuthLayoutComponent,
    children: [
      { path: 'login', component: LoginComponent },
      { path: 'register', component:  RegisterComponent }
    ],
  },

  // MAIN LAYOUT (navbar/footer)
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      { path: '', redirectTo: 'home', pathMatch: 'full' },
      { path: 'home', component: HomeComponent },
      { path: 'jobs', component: JobsListComponent },
      { path: 'users', component: UserComponent }
    ],
  },

  // Catch-all fallback
  { path: '**', redirectTo: 'home' }
];

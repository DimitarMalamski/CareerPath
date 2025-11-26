import { Routes } from '@angular/router';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { HomeComponent } from './features/home/home.component';
import { UserComponent } from './shared/user/user.component';
import { JobsListComponent } from './features/jobs/jobs-list/jobs-list.component';
import {AuthLayoutComponent} from './layout/auth-layout/auth-layout.component';
import {LoginComponent} from './features/auth/login/login.component';
import {RegisterComponent} from './features/auth/register/register.component';
import {ForgotPasswordComponent} from './features/auth/forgot-password/forgot-password.component';
import {ResetPasswordComponent} from './features/auth/reset-password/reset-password.component';
import {authGuard} from './core/guards/auth.guard';

export const routes: Routes = [
  // AUTH LAYOUT (no navbar/footer)
  {
    path: 'auth',
    component: AuthLayoutComponent,
    children: [
      { path: 'login', component: LoginComponent },
      { path: 'register', component:  RegisterComponent },
      { path: 'forgot-password', component: ForgotPasswordComponent },
      { path: 'reset-password', component: ResetPasswordComponent }
    ],
  },

  // MAIN LAYOUT (navbar/footer)
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      { path: '', redirectTo: 'home', pathMatch: 'full' },
      { path: 'home', component: HomeComponent },
      { path: 'jobs', component: JobsListComponent, canActivate: [authGuard] },
      { path: 'users', component: UserComponent, canActivate: [authGuard] }
    ],
  },

  // Catch-all fallback
  { path: '**', redirectTo: 'home' }
];

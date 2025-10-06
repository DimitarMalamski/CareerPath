import { Routes } from '@angular/router';
import { JobListComponent } from './components/job-list/job-list.component';
import { HomeComponent } from './components/home/home.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'job', component: JobListComponent },
  { path: '**', redirectTo: '' },
];

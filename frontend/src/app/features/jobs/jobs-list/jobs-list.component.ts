import {Component, inject, OnInit} from '@angular/core';
import { JobsService } from '../jobs.service';
import {RouterModule} from '@angular/router';
import {CommonModule} from '@angular/common';
import {JobRecommendation} from '../../../core/models/job-recommendation';
import {JobCardComponent} from '../job-card/job-card.component';
import {UserIdentityService} from '../../../core/services/user-identity.service';

@Component({
  selector: 'app-jobs-list',
  standalone: true,
  imports: [RouterModule, CommonModule, JobCardComponent],
  templateUrl: './jobs-list.component.html'
})
export class JobsListComponent implements OnInit {

  jobs: JobRecommendation[] = [];
  isLoading = true;

  private readonly jobsService = inject(JobsService);
  private readonly identity = inject(UserIdentityService);

  ngOnInit(): void {
    this.loadUserAndJobs();
  }

  private async loadUserAndJobs() {
    const userId = await this.identity.getUserId();

    if (!userId) {
      console.error("No authenticated user (Supabase session missing)");
      this.isLoading = false;
      return;
    }

    this.jobsService.getRecommendedJobs(userId).subscribe({
      next: (data) => {
        this.jobs = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.log('Error loading jobs:', err);
        this.isLoading = false;
      }
    });
  }
}

import { Component, inject, OnInit } from '@angular/core';
import { JobsService } from '../jobs.service';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { JobCardComponent } from '../job-card/job-card.component';
import { UserIdentityService } from '../../../core/services/user-identity.service';
import { Observable } from 'rxjs';
import { JobRecommendation } from '../../../core/models/job-recommendation';

@Component({
  selector: 'app-jobs-list',
  standalone: true,
  imports: [RouterModule, CommonModule, JobCardComponent],
  templateUrl: './jobs-list.component.html'
})
export class JobsListComponent implements OnInit {
  private readonly jobsService = inject(JobsService);
  private readonly identity = inject(UserIdentityService);

  jobs$ = this.jobsService.jobs$;
  isLoading = true;
  userId: string | null = null;

  ngOnInit(): void {
    this.loadUserAndJobs();
  }

  private async loadUserAndJobs(): Promise<void> {
    const userId = await this.identity.getUserId();

    if (!userId) {
      console.error('No authenticated user (Supabase session missing)');
      this.isLoading = false;
      return;
    }

    this.userId = userId;

    this.jobsService.getRecommendedJobs(userId).subscribe({
      next: () => {
        this.isLoading = false;
      },
      error: err => {
        console.error('Error loading jobs:', err);
        this.isLoading = false;
      }
    });
  }
}

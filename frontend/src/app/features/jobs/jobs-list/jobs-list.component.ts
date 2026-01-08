import { Component, inject, OnInit } from '@angular/core';
import { JobsService } from '../jobs.service';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { JobCardComponent } from '../job-card/job-card.component';
import { UserIdentityService } from '../../../core/services/user-identity.service';
import { map, Observable, BehaviorSubject, combineLatest } from 'rxjs';
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

  readonly pageSize = 3;
  readonly currentPage$ = new BehaviorSubject<number>(1);

  readonly pagedJobs$: Observable<JobRecommendation[]> = combineLatest([
    this.jobs$,
    this.currentPage$
  ]).pipe(
    map(([jobs, page]) => {
      const start = (page - 1) * this.pageSize;
      return jobs.slice(start, start + this.pageSize);
    })
  );

  readonly totalPages$: Observable<number> = this.jobs$.pipe(
    map(jobs => Math.ceil(jobs.length / this.pageSize))
  );

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

  goToPage(page: number): void {
    this.currentPage$.next(page);
  }

  nextPage(totalPages: number): void {
    const current = this.currentPage$.value;
    if (current < totalPages) {
      this.currentPage$.next(current + 1);
    }
  }

  prevPage(): void {
    const current = this.currentPage$.value;
    if (current > 1) {
      this.currentPage$.next(current - 1);
    }
  }
}

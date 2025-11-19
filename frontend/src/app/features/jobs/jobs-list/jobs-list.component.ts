import {Component, inject, OnInit} from '@angular/core';
import { JobsService } from '../jobs.service';
import {RouterModule} from '@angular/router';
import {CommonModule} from '@angular/common';
import {JobRecommendation} from '../../../core/models/job-recommendation';
import {JobCardComponent} from '../job-card/job-card.component';

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

  // Temporary user
  private readonly userId = '12c18193-4bd3-44f5-a085-07610ffa4fa7';

  ngOnInit(): void {
    this.jobsService.getRecommendedJobs(this.userId).subscribe({
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

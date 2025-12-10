import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, RouterModule} from '@angular/router';
import { JobDetailsService } from '../../../core/services/job-details.service';
import { JobDetails } from '../../../core/models/job-details';
import {CommonModule} from '@angular/common';
import {JobSummaryCardComponent} from './job-summary-card/job-summary-card.component';

@Component({
  selector: 'app-job-details-page',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    JobSummaryCardComponent
  ],
  templateUrl: './job-details-page.component.html',
})
export class JobDetailsPageComponent implements OnInit {

  job!: JobDetails;
  isLoading = true;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly jobDetailsService: JobDetailsService
  ) {}

  ngOnInit() {
    const jobId = this.route.snapshot.paramMap.get("jobId")!;
    const userId = this.route.snapshot.paramMap.get("userId")!;

    this.jobDetailsService.getJobDetails(jobId, userId).subscribe({
      next: (response) => {
        this.job = response;
        this.isLoading = false;
      },
      error: (err) => {
        console.error("Failed to load job details:", err);
        this.isLoading = false;
      }
    });
  }
}

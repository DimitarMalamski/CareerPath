import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, RouterModule} from '@angular/router';
import { JobDetails } from '../../../core/models/job-details';
import { JobListing } from '../../../core/models/job-listing';
import {CommonModule} from '@angular/common';
import {JobSummaryCardComponent} from './job-summary-card/job-summary-card.component';
import {JobSkillsBlockComponent} from './job-skills-block/job-skills-block.component';
import {JobDetailsService} from '../../../core/services/job-details.service';
import {JobHeaderComponent} from './job-header/job-header.component';
import {AiInsightsCardComponent} from './job-ai-insights-card/job-ai-insights-card.component';
import {JobDescriptionComponent} from './job-description/job-description.component';
import {RelatedJobsComponent} from './related-jobs/related-jobs.component';

@Component({
  selector: 'app-job-details-page',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    JobSummaryCardComponent,
    JobSkillsBlockComponent,
    JobHeaderComponent,
    AiInsightsCardComponent,
    JobDescriptionComponent,
    RelatedJobsComponent,
  ],
  templateUrl: './job-details-page.component.html',
})

export class JobDetailsPageComponent implements OnInit {
  state = {
    job: null as JobDetails | null,
    loading: true,
    error: null as string | null,
  };

  relatedJobs: JobListing[] = [];
  relatedLoading = true;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly jobDetailsService: JobDetailsService
  ) {}

  ngOnInit() {
    const data = this.route.snapshot.data;

    if (data['error']) {
      this.state.error = data['error'];
      this.state.loading = false;
      return;
    }

    const job = data['job'];

    if (!job) {
      this.state.error = "Failed to load job details.";
      this.state.loading = false;
      return;
    }

    this.state.job = job;
    this.state.loading = false;

    this.jobDetailsService.getRelatedJobs(job.id).subscribe({
      next: related => {
        this.relatedJobs = related;
        this.relatedLoading = false;
      },
      error: () => {
        this.relatedLoading = false;
      }
    });
  }
}

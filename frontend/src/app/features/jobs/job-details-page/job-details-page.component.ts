import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, RouterModule} from '@angular/router';
import { JobDetails } from '../../../core/models/job-details';
import {CommonModule} from '@angular/common';
import {JobSummaryCardComponent} from './job-summary-card/job-summary-card.component';
import {JobSkillsBlockComponent} from './job-skills-block/job-skills-block.component';

@Component({
  selector: 'app-job-details-page',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    JobSummaryCardComponent,
    JobSkillsBlockComponent,
  ],
  templateUrl: './job-details-page.component.html',
})
export class JobDetailsPageComponent implements OnInit {
  state = {
    job: null as JobDetails | null,
    loading: true,
    error: null as string | null
  };

  constructor(private readonly route: ActivatedRoute) {}

  ngOnInit() {
    const resolvedJob = this.route.snapshot.data['job'];

    if (!resolvedJob) {
      this.state.error = "Failed to load job details.";
      this.state.loading = false;
      return;
    }

    this.state.job = resolvedJob;
    this.state.loading = false;
  }
}

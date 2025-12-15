import { Component, Input } from '@angular/core';
import {RouterLink} from '@angular/router';
import {CommonModule} from '@angular/common';
import {RelatedJob} from '../../../../core/models/related-job';

@Component({
  selector: 'app-related-jobs',
  standalone: true,
  templateUrl: './related-jobs.component.html',
  imports: [
    RouterLink,
    CommonModule
  ]
})
export class RelatedJobsComponent {
  @Input() relatedJobs: RelatedJob[] = [];
  @Input() loading = false;
}

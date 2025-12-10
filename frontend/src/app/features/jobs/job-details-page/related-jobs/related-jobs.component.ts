import { Component, Input } from '@angular/core';
import {RouterLink} from '@angular/router';
import {CommonModule} from '@angular/common';

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
  @Input() relatedJobs: any[] = [];
  @Input() loading: boolean = false;
}

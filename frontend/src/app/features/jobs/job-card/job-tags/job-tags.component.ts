import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { JobRecommendation } from '../../../../core/models/job-recommendation';

@Component({
  selector: 'app-job-tags',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './job-tags.component.html'
})
export class JobTagsComponent {
  @Input() job!: JobRecommendation;

  formatJobType(type: string): string {
    if (!type) return '';

    return type
      .toLowerCase()
      .replace('_', ' ')
      .replaceAll(/\b\w/g, c => c.toUpperCase());
  }
}

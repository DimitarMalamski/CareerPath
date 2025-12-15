import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import {JobSummary} from '../../../../core/models/job-summary';

@Component({
  selector: 'app-job-summary-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './job-summary-card.component.html'
})
export class JobSummaryCardComponent {
  @Input() job!: JobSummary;
}

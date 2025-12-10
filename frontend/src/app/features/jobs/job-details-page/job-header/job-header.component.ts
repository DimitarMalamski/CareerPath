import { Component, Input } from '@angular/core';
import { JobDetails } from '../../../../core/models/job-details';
import {DatePipe} from '@angular/common';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-job-header',
  standalone: true,
  imports: [DatePipe, RouterLink],
  templateUrl: './job-header.component.html',
})
export class JobHeaderComponent {
  @Input() job!: JobDetails;
}

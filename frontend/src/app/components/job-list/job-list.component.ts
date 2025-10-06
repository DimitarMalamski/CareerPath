import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { JobService } from '../../services/job.service';
import { JobListing } from '../../models/job-listing';

@Component({
  selector: 'app-job-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './job-list.component.html'
})

export class JobListComponent implements OnInit {
  jobs: JobListing[] = [];
  loading = true;

  constructor(private jobService: JobService) { }

  ngOnInit(): void {
    this.jobService.getAll().subscribe({
      next: (data) => {
        this.jobs = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Failed to load jobs', err);
        this.loading = false;
      }
    });
  }
}

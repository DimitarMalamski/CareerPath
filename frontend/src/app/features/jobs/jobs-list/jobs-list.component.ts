import { Component, OnInit } from '@angular/core';
import { JobsService } from '../jobs.service';
import { JobListing } from '../../../core/models/job-listing';
import {RouterModule} from '@angular/router';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-jobs-list',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './jobs-list.component.html'
})

export class JobsListComponent implements OnInit {
  jobs: JobListing[] = [];
  isLoading = true;

  constructor(private readonly jobsService: JobsService) {}

  ngOnInit(): void {
    this.jobsService.getAllJobs().subscribe({
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

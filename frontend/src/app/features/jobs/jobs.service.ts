import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JobListing } from '../../core/models/job-listing';

@Injectable({
  providedIn: 'root'
})

export class JobsService {
  private readonly apiUrl = 'http://localhost:8080/api/jobs';

  constructor(private http: HttpClient) {}

  getAllJobs(): Observable<JobListing[]> {
    return this.http.get<JobListing[]>(this.apiUrl);
  }
}

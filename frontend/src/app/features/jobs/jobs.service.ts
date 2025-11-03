import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JobListing } from '../../core/models/job-listing';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})

export class JobsService {
  private readonly apiUrl = `${environment.apiUrl}/jobs`

  constructor(private readonly http: HttpClient) {}

  getAllJobs(): Observable<JobListing[]> {
    return this.http.get<JobListing[]>(this.apiUrl);
  }
}

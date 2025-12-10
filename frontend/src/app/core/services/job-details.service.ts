import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JobDetails } from '../models/job-details';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class JobDetailsService {

  private readonly apiUrl = environment.apiUrl + "/jobs";

  constructor(private readonly http: HttpClient) {}

  getJobDetails(jobId: string): Observable<JobDetails> {
    return this.http.get<JobDetails>(`${this.apiUrl}/${jobId}/details`);
  }
}

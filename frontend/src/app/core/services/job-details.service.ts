import {inject, Injectable} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';
import { JobDetails } from '../models/job-details';
import { JobListing } from '../models/job-listing';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class JobDetailsService {

  private readonly apiUrl = environment.apiUrl + "/jobs";

  private readonly http = inject(HttpClient);

  getJobDetails(jobId: string): Observable<JobDetails> {
    return this.http.get<JobDetails>(`${this.apiUrl}/${jobId}/details`).pipe(
      catchError(err => {
        return throwError(() => err);
      })
    );
  }

  getRelatedJobs(jobId: string): Observable<JobListing[]> {
    return this.http
      .get<JobListing[]>(`${this.apiUrl}/${jobId}/related`)
      .pipe(catchError(err => throwError(() => err)));
  }
}

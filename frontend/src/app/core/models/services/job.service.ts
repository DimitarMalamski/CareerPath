import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { JobListing } from '../models/job-listing';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environments';

@Injectable({ providedIn: 'root' })
export class JobService {
  private readonly  endpoint = 'api/jobs';
  private http = inject(HttpClient);

  getAll(): Observable<JobListing[]> {
    return this.http.get<JobListing[]>(`${environment.apiUrl}/${this.endpoint}`);
  }
}



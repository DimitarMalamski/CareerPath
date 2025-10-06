import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { JobListing } from '../models/job-listing';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class JobService {
  private readonly baseUrl = 'http://localhost:8080';
  private readonly  endpoint = 'jobs';

  constructor(private http: HttpClient) {}

  getAll(): Observable<JobListing[]> {
    return this.http.get<JobListing[]>(`${this.baseUrl}/${this.endpoint}`);
  }
}



import {inject, Injectable} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JobRecommendation } from '../../core/models/job-recommendation';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})

export class JobsService {
  private readonly apiUrl = `${environment.apiUrl}/jobs`

  private readonly http = inject(HttpClient);

  getRecommendedJobs(userId: string): Observable<JobRecommendation[]> {
    return this.http.get<JobRecommendation[]>(`${this.apiUrl}/recommendations/${userId}`);
  }
}

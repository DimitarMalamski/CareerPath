import {inject, Injectable} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {BehaviorSubject, Observable, tap} from 'rxjs';
import { JobRecommendation } from '../../core/models/job-recommendation';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})

export class JobsService {
  private readonly apiUrl = `${environment.apiUrl}/jobs`
  private readonly http = inject(HttpClient);

  private readonly jobsSubject =
    new BehaviorSubject<JobRecommendation[]>([]);
  readonly jobs$ = this.jobsSubject.asObservable();

  private lastUserId: string | null = null;

  getRecommendedJobs(userId: string): Observable<JobRecommendation[]> {
    this.lastUserId = userId;

    return this.http
      .get<JobRecommendation[]>(`${this.apiUrl}/recommendations/${userId}`)
      .pipe(
          tap(jobs => this.jobsSubject.next(jobs))
      );
  }

  reload(): void {
    if (!this.lastUserId) return;

    this.http
      .get<JobRecommendation[]>(`${this.apiUrl}/recommendations/${this.lastUserId}`)
      .subscribe(jobs => this.jobsSubject.next(jobs));
  }

  addJob(job: JobRecommendation): void {
    const currentJobs = this.jobsSubject.value;
    this.jobsSubject.next([job, ...currentJobs]);
  }
}

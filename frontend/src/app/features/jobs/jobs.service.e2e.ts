import { Injectable } from '@angular/core'
import { BehaviorSubject } from 'rxjs'
import { JobRecommendation } from '../../core/models/job-recommendation';

@Injectable()
export class JobsServiceE2e {
  private readonly _jobs$ = new BehaviorSubject<JobRecommendation[]>([
    {
      id: 'job-1',
      title: 'Junior Backend Developer',
      company: 'CareerPath',
      location: 'Eindhoven, NL',
      finalScore: 0.87,
      skills: ['Java', 'Spring Boot'],
    } as JobRecommendation,
    {
      id: 'job-2',
      title: 'Frontend Engineer',
      company: 'CareerPath',
      location: 'Remote',
      finalScore: 0.82,
      skills: ['Angular', 'TypeScript'],
    } as JobRecommendation,
  ])

  jobs$ = this._jobs$.asObservable()

  getRecommendedJobs() {
    return this.jobs$
  }
}

import { TestBed } from '@angular/core/testing'
import { firstValueFrom } from 'rxjs'

import { JobsServiceE2e } from './jobs.service.e2e'

describe('JobsServiceE2e (E2E stub)', () => {
  let service: JobsServiceE2e

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [JobsServiceE2e],
    })

    service = TestBed.inject(JobsServiceE2e)
  })

  it('should be created', () => {
    expect(service).toBeTruthy()
  })

  it('should expose recommended jobs as an observable', async () => {
    const jobs = await firstValueFrom(service.getRecommendedJobs())

    expect(Array.isArray(jobs)).toBe(true)
    expect(jobs.length).toBe(2)
  })

  it('should return the seeded job recommendations (partial match)', async () => {
    const jobs = await firstValueFrom(service.getRecommendedJobs())

    expect(jobs[0]).toMatchObject({
      id: 'job-1',
      title: 'Junior Backend Developer',
      company: 'CareerPath',
      location: 'Eindhoven, NL',
      finalScore: 0.87,
      skills: ['Java', 'Spring Boot'],
    })

    expect(jobs[1]).toMatchObject({
      id: 'job-2',
      title: 'Frontend Engineer',
      company: 'CareerPath',
      location: 'Remote',
      finalScore: 0.82,
      skills: ['Angular', 'TypeScript'],
    })
  })
})

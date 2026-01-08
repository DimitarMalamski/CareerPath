import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { JobsService } from './jobs.service';
import { environment } from '../../../environments/environment';
import { JobRecommendation } from '../../core/models/job-recommendation';
import { expect } from 'vitest';

describe('JobsService', () => {
  let service: JobsService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [JobsService],
    });

    service = TestBed.inject(JobsService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should fetch recommended jobs by userId', () => {
    const mockJobs: JobRecommendation[] = [
      {
        id: '1',
        title: 'Backend Dev',
        company: 'Google',
        location: 'Berlin',
        type: 'FULL_TIME',
        skills: ['Java'],
        description: 'desc',
        finalScore: 0.9,
        aiExplanation: 'Great match.',
        createdAt: '2025-01-01',
        matchedSkills: ['Java'],
        missingSkills: []
      }
    ];

    const userId = '12345';
    let result: JobRecommendation[] = [];

    service.getRecommendedJobs(userId).subscribe((data) => {
      result = data;
    });

    const req = httpMock.expectOne(
      `${environment.apiUrl}/jobs/recommendations/${userId}`
    );

    expect(req.request.method).toBe('GET');

    req.flush(mockJobs);

    expect(result.length).toBe(1);
    expect(result[0].title).toBe('Backend Dev');
  });

  it('reload should do nothing if no userId is set', () => {
    service.reload();

    httpMock.expectNone(`${environment.apiUrl}/jobs/recommendations/`);

    service.jobs$.subscribe(jobs => {
      expect(jobs).toEqual([]);
    });
  });

  it('reload should refetch jobs for last userId and update jobs$', () => {
    const userId = '123';
    const apiJobs: JobRecommendation[] = [
      {
        id: '1',
        title: 'Backend Dev',
        company: 'Google',
        location: 'Berlin',
        type: 'FULL_TIME',
        skills: ['Java'],
        description: 'desc',
        finalScore: 0.9,
        aiExplanation: '',
        createdAt: '2025-01-01',
        matchedSkills: ['Java'],
        missingSkills: []
      }
    ];

    service.getRecommendedJobs(userId).subscribe();
    httpMock.expectOne(
      `${environment.apiUrl}/jobs/recommendations/${userId}`
    ).flush([]);

    service.reload();

    const reloadReq = httpMock.expectOne(
      `${environment.apiUrl}/jobs/recommendations/${userId}`
    );

    reloadReq.flush(apiJobs);

    service.jobs$.subscribe(jobs => {
      expect(jobs.length).toBe(1);
      expect(jobs[0].id).toBe('1');
    });
  });

  it('should merge fetched jobs with existing jobs without duplicates', () => {
    const userId = '123';

    const existingJob: JobRecommendation = {
      id: 'existing',
      title: 'WS Job',
      company: 'WS',
      location: 'Remote',
      type: 'FULL_TIME',
      skills: ['TS'],
      description: 'from ws',
      finalScore: 0.7,
      aiExplanation: '',
      createdAt: '2025-01-01',
      matchedSkills: [],
      missingSkills: []
    };

    const apiJob: JobRecommendation = {
      id: 'api',
      title: 'API Job',
      company: 'API',
      location: 'Berlin',
      type: 'FULL_TIME',
      skills: ['Java'],
      description: 'from api',
      finalScore: 0.9,
      aiExplanation: '',
      createdAt: '2025-01-01',
      matchedSkills: [],
      missingSkills: []
    };

    service.getRecommendedJobs(userId).subscribe();
    httpMock.expectOne(
      `${environment.apiUrl}/jobs/recommendations/${userId}`
    ).flush([existingJob]);

    service.getRecommendedJobs(userId).subscribe();
    httpMock.expectOne(
      `${environment.apiUrl}/jobs/recommendations/${userId}`
    ).flush([apiJob]);

    service.jobs$.subscribe(jobs => {
      expect(jobs.length).toBe(2);
      expect(jobs.map(j => j.id)).toContain('existing');
      expect(jobs.map(j => j.id)).toContain('api');
    });
  });
});

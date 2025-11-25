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
});

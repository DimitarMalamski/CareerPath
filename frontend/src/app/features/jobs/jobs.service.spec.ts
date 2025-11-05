import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController  } from '@angular/common/http/testing';
import { JobsService } from './jobs.service';
import { environment } from '../../../environments/environment';
import { JobListing } from '../../core/models/job-listing';
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

  it('should fetch all jobs from API', () => {
    const mockJobs: JobListing[] = [
      {
        id: '1',
        title: 'Backend Dev',
        company: 'Google',
        location: 'Berlin',
        stackSummary: 'Java, Spring Boot',
        type: 'FULL_TIME',
        status: 'PUBLISHED',
        expiresAt: '2025-12-31',
        skills: ['Java']
      },
      {
        id: '2',
        title: 'Frontend Dev',
        company: 'Meta',
        location: 'Amsterdam',
        stackSummary: 'Angular, TypeScript',
        type: 'INTERNSHIP',
        status: 'PUBLISHED',
        expiresAt: '2025-12-31',
        skills: ['Angular']
      }
    ];

    let result: JobListing[] = [];
    service.getAllJobs().subscribe((jobListings) => (result = jobListings));

    const req = httpMock.expectOne(`${environment.apiUrl}/jobs`);
    expect(req.request.method).toBe('GET');

    req.flush(mockJobs);

    expect(result.length).toBe(2);
    expect(result[0].title).toBe('Backend Dev');
  });
});

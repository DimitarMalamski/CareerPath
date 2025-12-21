import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';
import { expect } from 'vitest';

import { JobDetailsService } from '../job-details.service';
import { environment } from '../../../../environments/environment';
import { JobDetails } from '../../models/job-details';
import { JobListing } from '../../models/job-listing';

describe('JobDetailsService', () => {
  let service: JobDetailsService;
  let httpMock: HttpTestingController;

  const apiUrl = environment.apiUrl + '/jobs';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [JobDetailsService]
    });

    service = TestBed.inject(JobDetailsService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch job details successfully', () => {
    const mockDetails: JobDetails = {
      id: '1',
      title: 'Backend Dev',
      description: 'Some description'
    } as JobDetails;

    service.getJobDetails('1').subscribe(result => {
      expect(result).toEqual(mockDetails);
    });

    const req = httpMock.expectOne(`${apiUrl}/1/details`);
    expect(req.request.method).toBe('GET');

    req.flush(mockDetails);
  });

  it('should propagate error when getJobDetails fails', () => {
    service.getJobDetails('1').subscribe({
      next: () => {
        throw new Error('Expected error, but got success');
      },
      error: err => {
        expect(err.status).toBe(500);
      }
    });

    const req = httpMock.expectOne(`${apiUrl}/1/details`);
    req.flush('Server error', { status: 500, statusText: 'Server Error' });
  });

  it('should fetch related jobs successfully', () => {
    const mockJobs: JobListing[] = [
      { id: '2', title: 'Frontend Dev' } as JobListing
    ];

    service.getRelatedJobs('1').subscribe(result => {
      expect(result).toEqual(mockJobs);
    });

    const req = httpMock.expectOne(`${apiUrl}/1/related`);
    expect(req.request.method).toBe('GET');

    req.flush(mockJobs);
  });

  it('should propagate error when getRelatedJobs fails', () => {
    service.getRelatedJobs('1').subscribe({
      next: () => {
        throw new Error('Expected error, but got success');
      },
      error: err => {
        expect(err.status).toBe(404);
      }
    });

    const req = httpMock.expectOne(`${apiUrl}/1/related`);
    req.flush('Not found', { status: 404, statusText: 'Not Found' });
  });
});

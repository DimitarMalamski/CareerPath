import { ComponentFixture, TestBed } from '@angular/core/testing';
import {of, throwError} from 'rxjs';
import { expect, vi } from 'vitest';
import { JobsListComponent } from './jobs-list.component';
import { JobsService } from '../jobs.service';
import { JobListing } from '../../../core/models/job-listing';

describe('JobsListComponent', () => {
  let fixture: ComponentFixture<JobsListComponent>;
  let component: JobsListComponent;
  let jobsServiceMock: { getAllJobs: ReturnType<typeof vi.fn> };

  beforeEach(async () => {
    jobsServiceMock = {
      getAllJobs: vi.fn(),
    };

    await TestBed.configureTestingModule({
      imports: [JobsListComponent],
      providers: [{ provide: JobsService, useValue: jobsServiceMock }],
    }).compileComponents();

    fixture = TestBed.createComponent(JobsListComponent);
    component = fixture.componentInstance;
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should load jobs on init and update the state', () => {
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
        skills: ['Java'],
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
        skills: ['Angular'],
      },
    ];

    jobsServiceMock.getAllJobs.mockReturnValue(of(mockJobs));

    fixture.detectChanges();

    expect(component.isLoading).toBe(false);
    expect(component.jobs.length).toBe(2);
    expect(component.jobs[0].title).toBe('Backend Dev');
  })

  it('should log error and set isLoading=false when API fails', () => {
    const consoleSpy = vi.spyOn(console, 'log').mockImplementation(() => undefined);
    jobsServiceMock.getAllJobs.mockReturnValue(throwError(() => new Error(('API error'))));

    fixture.detectChanges();

    expect(consoleSpy).toHaveBeenCalledWith(
      'Error loading jobs:',
      expect.any(Error)
    );

    expect(component.isLoading).toBe(false);
    consoleSpy.mockRestore();
  });
});

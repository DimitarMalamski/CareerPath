import { ComponentFixture, TestBed } from '@angular/core/testing';
import { expect, vi } from 'vitest';
import { of, throwError } from 'rxjs';
import { ActivatedRoute } from '@angular/router';

import { JobDetailsPageComponent } from './job-details-page.component';
import { JobDetailsService } from '../../../core/services/job-details.service';
import { provideMockSupabase } from '../../../../testing/mock-supabase';

describe('JobDetailsPageComponent', () => {
  let fixture: ComponentFixture<JobDetailsPageComponent>;
  let component: JobDetailsPageComponent;

  let jobDetailsServiceMock: {
    getRelatedJobs: ReturnType<typeof vi.fn>
  };

  const mockJob = {
    id: 'job-123',
    title: 'Backend Developer',
    aiExplanation: 'Test AI explanation',
    description: 'This is a backend job.',
    skills: ['Java', 'Spring'],
  };

  beforeEach(async () => {
    vi.useFakeTimers();

    jobDetailsServiceMock = {
      getRelatedJobs: vi.fn()
    };

    await TestBed.configureTestingModule({
      imports: [JobDetailsPageComponent],
      providers: [
        provideMockSupabase(),
        { provide: JobDetailsService, useValue: jobDetailsServiceMock },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              data: {
                job: mockJob,
                error: null
              }
            }
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(JobDetailsPageComponent);
    component = fixture.componentInstance;
  });

  afterEach(() => {
    vi.useRealTimers();
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should load job details from resolver on init', async () => {
    jobDetailsServiceMock.getRelatedJobs.mockReturnValue(of([]));

    fixture.detectChanges();
    await fixture.whenStable();
    await vi.runAllTimersAsync();

    expect(component.state.job).toEqual(mockJob);
    expect(component.state.loading).toBe(false);
  });

  it('should handle resolver error state', async () => {
    TestBed.resetTestingModule();

    await TestBed.configureTestingModule({
      imports: [JobDetailsPageComponent],
      providers: [
        provideMockSupabase(),
        { provide: JobDetailsService, useValue: jobDetailsServiceMock },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              data: {
                job: null,
                error: 'Something failed'
              }
            }
          }
        }
      ]
    }).compileComponents();

    const errorFixture = TestBed.createComponent(JobDetailsPageComponent);
    const errorComp = errorFixture.componentInstance;

    errorFixture.detectChanges();
    await errorFixture.whenStable();

    expect(errorComp.state.error).toBe('Something failed');
    expect(errorComp.state.loading).toBe(false);
  });

  it('should fetch related jobs after loading details', async () => {
    const relatedMock = [
      { id: 'r1', title: 'DevOps Engineer' },
      { id: 'r2', title: 'Cloud Engineer' }
    ];

    jobDetailsServiceMock.getRelatedJobs.mockReturnValue(of(relatedMock));

    fixture.detectChanges();
    await fixture.whenStable();
    await vi.runAllTimersAsync();

    expect(jobDetailsServiceMock.getRelatedJobs).toHaveBeenCalledWith('job-123');
    expect(component.relatedJobs.length).toBe(2);
    expect(component.relatedLoading).toBe(false);
  });

  it('should handle error when related jobs request fails', async () => {
    jobDetailsServiceMock.getRelatedJobs.mockReturnValue(
      throwError(() => new Error('Network error'))
    );

    fixture.detectChanges();
    await fixture.whenStable();
    await vi.runAllTimersAsync();

    expect(component.relatedLoading).toBe(false);
    expect(component.relatedJobs.length).toBe(0);
  });

  it('should handle missing job without resolver error', async () => {
    TestBed.resetTestingModule();

    await TestBed.configureTestingModule({
      imports: [JobDetailsPageComponent],
      providers: [
        provideMockSupabase(),
        { provide: JobDetailsService, useValue: jobDetailsServiceMock },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              data: {
                job: null,
                error: null
              }
            }
          }
        }
      ]
    }).compileComponents();

    const missingJobFixture = TestBed.createComponent(JobDetailsPageComponent);
    const missingJobComp = missingJobFixture.componentInstance;

    missingJobFixture.detectChanges();
    await missingJobFixture.whenStable();

    expect(missingJobComp.state.error).toBe('Failed to load job details.');
    expect(missingJobComp.state.loading).toBe(false);
    expect(jobDetailsServiceMock.getRelatedJobs).not.toHaveBeenCalled();
  });
});

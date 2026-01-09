import { ComponentFixture, TestBed } from '@angular/core/testing';
import {of, throwError, firstValueFrom, Observable} from 'rxjs';
import { expect, vi } from 'vitest';
import { JobsListComponent } from './jobs-list.component';
import { JobsService } from '../jobs.service';
import { UserIdentityService } from '../../../core/services/user-identity.service';
import { provideMockSupabase } from '../../../../testing/mock-supabase';
import {JobRecommendation} from '../../../core/models/job-recommendation';
import { BehaviorSubject } from 'rxjs';

describe('JobsListComponent', () => {
  let fixture: ComponentFixture<JobsListComponent>;
  let component: JobsListComponent;
  let jobsSubject: BehaviorSubject<JobRecommendation[]>;

  let jobsServiceMock: {
    getRecommendedJobs: ReturnType<typeof vi.fn>;
    jobs$: Observable<JobRecommendation[]>;
  };

  let identityMock: { getUserId: ReturnType<typeof vi.fn> };

  beforeEach(async () => {
    vi.useFakeTimers();

    jobsSubject = new BehaviorSubject<JobRecommendation[]>([]);

    jobsServiceMock = {
      getRecommendedJobs: vi.fn(),
      jobs$: jobsSubject.asObservable()
    };

    identityMock = {
      getUserId: vi.fn().mockResolvedValue('test-user-id')
    };

    await TestBed.configureTestingModule({
      imports: [JobsListComponent],
      providers: [
        { provide: JobsService, useValue: jobsServiceMock },
        { provide: UserIdentityService, useValue: identityMock },
        provideMockSupabase()
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(JobsListComponent);
    component = fixture.componentInstance;
  });

  afterEach(() => {
    vi.useRealTimers();
  });

  it('should load jobs on init and update the state', async () => {
    const mockJob = (
      overrides?: Partial<JobRecommendation>
    ): JobRecommendation => ({
      id: '1',
      title: 'Backend Dev',
      company: 'Test Corp',
      location: 'Remote',
      type: 'FULL_TIME',
      skills: [],
      description: 'Test job',
      matchedSkills: [],
      missingSkills: [],
      finalScore: 80,
      aiExplanation: 'Good match',
      createdAt: new Date().toISOString(),
      ...overrides
    });

    const mockJobs: JobRecommendation[] = [
      mockJob({ id: '1' }),
      mockJob({ id: '2', title: 'Frontend Dev' })
    ];

    jobsServiceMock.getRecommendedJobs.mockImplementation(() => {
      jobsSubject.next(mockJobs); // ✅ THIS IS THE KEY
      return of(mockJobs);
    });

    fixture.detectChanges();
    await fixture.whenStable();

    const jobs = await firstValueFrom(component.jobs$);

    expect(component.isLoading).toBe(false);
    expect(jobs.length).toBe(2);
  });

  it('should log error and set isLoading=false when API fails', async () => {
    const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => undefined);

    jobsServiceMock.getRecommendedJobs.mockReturnValue(
      throwError(() => new Error('API error'))
    );

    fixture.detectChanges();
    await fixture.whenStable();
    await vi.runAllTimersAsync();

    expect(component.isLoading).toBe(false);

    consoleSpy.mockRestore();
  });

  it('should stop loading when no userId is returned', async () => {
    const consoleErrorSpy = vi.spyOn(console, 'error').mockImplementation(() => undefined);

    identityMock.getUserId.mockResolvedValue(null);

    fixture.detectChanges();
    await fixture.whenStable();
    await vi.runAllTimersAsync();

    expect(consoleErrorSpy).toHaveBeenCalledWith(
      'No authenticated user (Supabase session missing)'
    );

    expect(component.isLoading).toBe(false);

    const jobs = await firstValueFrom(component.jobs$);
    expect(jobs.length).toBe(0);

    consoleErrorSpy.mockRestore();
  });
});

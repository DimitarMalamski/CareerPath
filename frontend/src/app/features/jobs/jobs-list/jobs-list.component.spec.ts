import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { expect, vi } from 'vitest';
import { JobsListComponent } from './jobs-list.component';
import { JobsService } from '../jobs.service';
import { UserIdentityService } from '../../../core/services/user-identity.service';
import { provideMockSupabase } from '../../../../testing/mock-supabase';

describe('JobsListComponent', () => {
  let fixture: ComponentFixture<JobsListComponent>;
  let component: JobsListComponent;

  let jobsServiceMock: { getRecommendedJobs: ReturnType<typeof vi.fn> };
  let identityMock: { getUserId: ReturnType<typeof vi.fn> };

  beforeEach(async () => {
    vi.useFakeTimers();

    jobsServiceMock = {
      getRecommendedJobs: vi.fn()
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
    const mockJobs = [
      { id: '1', title: 'Backend Dev' },
      { id: '2', title: 'Frontend Dev' }
    ];

    jobsServiceMock.getRecommendedJobs.mockReturnValue(of(mockJobs));

    fixture.detectChanges();
    await fixture.whenStable();
    await vi.runAllTimersAsync(); // <-- ensures subscribe runs

    expect(component.isLoading).toBe(false);
    expect(component.jobs.length).toBe(2);
  });

  it('should log error and set isLoading=false when API fails', async () => {
    const consoleSpy = vi.spyOn(console, 'log').mockImplementation(() => undefined);

    jobsServiceMock.getRecommendedJobs.mockReturnValue(
      throwError(() => new Error('API error'))
    );

    fixture.detectChanges();
    await fixture.whenStable();
    await vi.runAllTimersAsync();

    expect(consoleSpy).toHaveBeenCalledWith(
      'Error loading jobs:',
      expect.any(Error)
    );

    expect(component.isLoading).toBe(false);

    consoleSpy.mockRestore();
  });
});

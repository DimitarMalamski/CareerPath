import { TestBed } from '@angular/core/testing';
import {
  ActivatedRouteSnapshot,
  Router,
  RouterStateSnapshot
} from '@angular/router';
import {of, throwError, lastValueFrom, isObservable} from 'rxjs';
import { expect, vi } from 'vitest';

import { jobDetailsResolver } from '../job-details.resolver';
import { JobDetailsService } from '../../services/job-details.service';
import { JobDetails } from '../../models/job-details';

function resolveResult<T>(value: T | Promise<T> | import('rxjs').Observable<T>) {
  return isObservable(value) ? value : of(value);
}

describe('jobDetailsResolver', () => {
  let jobDetailsServiceMock: { getJobDetails: ReturnType<typeof vi.fn> };
  let routerMock: { navigate: ReturnType<typeof vi.fn> };

  const state = {} as RouterStateSnapshot;

  beforeEach(() => {
    jobDetailsServiceMock = {
      getJobDetails: vi.fn()
    };

    routerMock = {
      navigate: vi.fn()
    };

    TestBed.configureTestingModule({
      providers: [
        { provide: JobDetailsService, useValue: jobDetailsServiceMock },
        { provide: Router, useValue: routerMock }
      ]
    });
  });

  function createRoute(jobId: string | null): ActivatedRouteSnapshot {
    return {
      paramMap: {
        get: () => jobId
      }
    } as unknown as ActivatedRouteSnapshot;
  }

  it('should navigate to not-found when jobId is missing', async () => {
    const route = createRoute(null);

    const result = await lastValueFrom(
      resolveResult(
        TestBed.runInInjectionContext(() =>
          jobDetailsResolver(route, state)
        )
      )
    );

    expect(routerMock.navigate).toHaveBeenCalledWith(['/not-found']);
    expect(result).toBeNull();
  });

  it('should return job details when service succeeds', async () => {
    const mockDetails: JobDetails = {
      id: '1',
      title: 'Backend Dev'
    } as JobDetails;

    jobDetailsServiceMock.getJobDetails.mockReturnValue(of(mockDetails));

    const route = createRoute('1');

    const result = await lastValueFrom(
      resolveResult(
        TestBed.runInInjectionContext(() =>
          jobDetailsResolver(route, state)
        )
      )
    );

    expect(jobDetailsServiceMock.getJobDetails).toHaveBeenCalledWith('1');
    expect(result).toEqual(mockDetails);
  });

  it('should navigate to not-found when service returns 404', async () => {
    jobDetailsServiceMock.getJobDetails.mockReturnValue(
      throwError(() => ({ status: 404 }))
    );

    const route = createRoute('1');

    const result = await lastValueFrom(
      resolveResult(
        TestBed.runInInjectionContext(() =>
          jobDetailsResolver(route, state)
        )
      )
    );

    expect(routerMock.navigate).toHaveBeenCalledWith(['/not-found']);
    expect(result).toBeNull();
  });

  it('should return null without redirect for non-404 errors', async () => {
    jobDetailsServiceMock.getJobDetails.mockReturnValue(
      throwError(() => ({ status: 500 }))
    );

    const route = createRoute('1');

    const result = await lastValueFrom(
      resolveResult(
        TestBed.runInInjectionContext(() =>
          jobDetailsResolver(route, state)
        )
      )
    );

    expect(routerMock.navigate).not.toHaveBeenCalled();
    expect(result).toBeNull();
  });
});

import { inject } from '@angular/core';
import { ResolveFn, ActivatedRouteSnapshot, Router } from '@angular/router';
import { JobDetailsService } from '../services/job-details.service';
import { JobDetails } from '../models/job-details';
import { catchError, of } from 'rxjs';

export const jobDetailsResolver: ResolveFn<JobDetails | null> = (route: ActivatedRouteSnapshot) => {
  const jobDetailsService = inject(JobDetailsService);
  const router = inject(Router);

  const jobId = route.paramMap.get("jobId")!;

  return jobDetailsService.getJobDetails(jobId).pipe(
    catchError(err => {
      console.error("Resolver failed to load job:", err);
      router.navigate(['/jobs'], { queryParams: { error: 'load_failed' } });
      return of(null);
    })
  );
};

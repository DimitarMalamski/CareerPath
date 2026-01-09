import { TestBed } from '@angular/core/testing'
import { PLATFORM_ID } from '@angular/core'
import { firstValueFrom, skip } from 'rxjs'

import { JobWebsocketService } from '../job-websocket.service';
import { JobsService } from '../../../features/jobs/jobs.service';
import { Router } from '@angular/router'

describe('JobWebsocketService (Option B)', () => {
  let service: JobWebsocketService
  let jobsService: { reload: ReturnType<typeof vi.fn> }
  let router: { url: string }

  beforeEach(() => {
    jobsService = {
      reload: vi.fn(),
    }

    router = {
      url: '/jobs',
    }

    TestBed.configureTestingModule({
      providers: [
        JobWebsocketService,
        { provide: JobsService, useValue: jobsService },
        { provide: Router, useValue: router },
        { provide: PLATFORM_ID, useValue: 'server' },
      ],
    })

    service = TestBed.inject(JobWebsocketService)
  })

  it('should be created', () => {
    expect(service).toBeTruthy()
  })

  it('should NOT connect when not running in the browser', async () => {
    await service.connect()

    expect(jobsService.reload).not.toHaveBeenCalled()
  })

  it('should expose newJob$ with initial value false', async () => {
    const value = await firstValueFrom(service.newJob$)

    expect(value).toBe(false)
  })

  it('should set newJob$ to false when clearNewJobIndicator is called', async () => {
    const nextValuePromise = firstValueFrom(
      service.newJob$.pipe(skip(1))
    )

    service.clearNewJobIndicator()

    const value = await nextValuePromise
    expect(value).toBe(false)
  })

  it('should be idempotent when connect is called multiple times on server', async () => {
    await service.connect()
    await service.connect()
    await service.connect()

    expect(jobsService.reload).not.toHaveBeenCalled()
  })
})

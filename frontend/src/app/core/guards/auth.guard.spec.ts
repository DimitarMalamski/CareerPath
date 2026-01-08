import { TestBed } from '@angular/core/testing'
import { Router } from '@angular/router'
import type { ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router'
import { authGuard } from './auth.guard'
import { SupabaseService } from '../services/supabase.service'
import { environment } from '../../../environments/environment'

describe('authGuard', () => {
  let supabaseService: { getSession: ReturnType<typeof vi.fn> }
  let router: { navigate: ReturnType<typeof vi.fn> }

  // Dummy route & state required by CanActivateFn
  const route = {} as ActivatedRouteSnapshot
  const state = {} as RouterStateSnapshot

  beforeEach(() => {
    supabaseService = {
      getSession: vi.fn(),
    }

    router = {
      navigate: vi.fn(),
    }

    TestBed.configureTestingModule({
      providers: [
        { provide: SupabaseService, useValue: supabaseService },
        { provide: Router, useValue: router },
      ],
    })
  })

  afterEach(() => {
    environment.e2e = false
    vi.restoreAllMocks()
  })

  it('should allow access when running in E2E mode', async () => {
    environment.e2e = true

    const result = await TestBed.runInInjectionContext(() =>
      authGuard(route, state)
    )

    expect(result).toBe(true)
    expect(supabaseService.getSession).not.toHaveBeenCalled()
    expect(router.navigate).not.toHaveBeenCalled()
  })

  it('should redirect to login when no session exists', async () => {
    environment.e2e = false
    supabaseService.getSession.mockResolvedValue(null)

    const result = await TestBed.runInInjectionContext(() =>
      authGuard(route, state)
    )

    expect(supabaseService.getSession).toHaveBeenCalled()
    expect(router.navigate).toHaveBeenCalledWith(['/auth/login'])
    expect(result).toBe(false)
  })

  it('should allow access when session exists', async () => {
    environment.e2e = false
    supabaseService.getSession.mockResolvedValue({ user: { id: 'user-1' } })

    const result = await TestBed.runInInjectionContext(() =>
      authGuard(route, state)
    )

    expect(supabaseService.getSession).toHaveBeenCalled()
    expect(router.navigate).not.toHaveBeenCalled()
    expect(result).toBe(true)
  })
})

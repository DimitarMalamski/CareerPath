import { TestBed } from '@angular/core/testing'
import { expect, vi } from 'vitest'
import type { Session, User } from '@supabase/supabase-js'

import { UserIdentityService } from '../user-identity.service'
import { SupabaseService } from '../supabase.service'
import { environment } from '../../../../environments/environment'

describe('UserIdentityService', () => {
  let service: UserIdentityService
  let supabaseService: SupabaseService

  /* ---------- Mock factories ---------- */

  const mockUser = (overrides?: Partial<User>): User => ({
    id: 'user-123',
    aud: 'authenticated',
    app_metadata: {},
    user_metadata: {},
    created_at: new Date().toISOString(),
    email: 'test@test.com',
    ...overrides,
  })

  const mockSession = (overrides?: Partial<Session>): Session => ({
    access_token: 'access-token',
    refresh_token: 'refresh-token',
    expires_in: 3600,
    token_type: 'bearer',
    user: mockUser(),
    ...overrides,
  })

  /* ---------- Setup ---------- */

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        UserIdentityService,
        {
          provide: SupabaseService,
          useValue: {
            getSession: vi.fn(),
          },
        },
      ],
    })

    service = TestBed.inject(UserIdentityService)
    supabaseService = TestBed.inject(SupabaseService)
  })

  afterEach(() => {
    environment.e2e = false
    vi.restoreAllMocks()
  })

  /* ---------- Tests ---------- */

  it('should be created', () => {
    expect(service).toBeTruthy()
  })

  it('should return user id from session', async () => {
    vi.spyOn(supabaseService, 'getSession').mockResolvedValue(
      mockSession({
        user: mockUser({ id: 'user-123' }),
      })
    )

    const result = await service.getUserId()
    expect(result).toBe('user-123')
  })

  it('should return email from session', async () => {
    vi.spyOn(supabaseService, 'getSession').mockResolvedValue(
      mockSession({
        user: mockUser({ email: 'mail@test.com' }),
      })
    )

    const result = await service.getEmail()
    expect(result).toBe('mail@test.com')
  })

  it('should return null when no session exists', async () => {
    vi.spyOn(supabaseService, 'getSession').mockResolvedValue(null)

    const result = await service.getUserId()
    expect(result).toBeNull()
  })
})

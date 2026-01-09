import { supabaseClientE2eProvider } from '../supabase-client.e2e.provider'
import { SUPABASE_CLIENT } from '../../supabase-client.token'
import { ValueProvider } from '@angular/core'

interface SupabaseClientE2e {
  auth: {
    signInWithPassword: () => Promise<unknown>
    signUp: () => Promise<unknown>
    signOut: () => Promise<unknown>
    getSession: () => Promise<{
      data: {
        session: {
          user: {
            id: string
            email: string
          }
        }
      }
    }>
  }
}

describe('supabaseClientE2eProvider (coverage only)', () => {
  it('should provide SUPABASE_CLIENT with auth methods', async () => {
    const provider = supabaseClientE2eProvider as ValueProvider

    expect(provider.provide).toBe(SUPABASE_CLIENT)
    expect(provider.useValue).toBeTruthy()

    const client = provider.useValue as SupabaseClientE2e

    expect(client.auth).toBeTruthy()

    await client.auth.signInWithPassword()
    await client.auth.signUp()
    await client.auth.signOut()

    const sessionResult = await client.auth.getSession()

    expect(sessionResult.data.session.user.email).toBe('e2e@careerpath.dev')
  })
})

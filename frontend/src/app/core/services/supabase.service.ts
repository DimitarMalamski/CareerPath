import { Injectable, inject } from '@angular/core'
import type { SupabaseClient } from '@supabase/supabase-js'
import { SUPABASE_CLIENT } from '../supabase-client.token'

@Injectable({ providedIn: 'root' })
export class SupabaseService {
  private readonly client = inject<SupabaseClient>(SUPABASE_CLIENT)

  signIn(email: string, password: string) {
    return this.client.auth.signInWithPassword({ email, password })
  }

  signUp(email: string, password: string) {
    return this.client.auth.signUp({ email, password })
  }

  signOut() {
    return this.client.auth.signOut()
  }

  async getSession() {
    const { data } = await this.client.auth.getSession()
    return data.session
  }

  getClient(): SupabaseClient {
    return this.client
  }
}

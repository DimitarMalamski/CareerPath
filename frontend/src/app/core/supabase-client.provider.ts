import { Provider } from '@angular/core'
import { createClient, SupabaseClient } from '@supabase/supabase-js'
import { environment } from '../../environments/environment'
import { SUPABASE_CLIENT } from './supabase-client.token'

export const supabaseClientProvider: Provider = {
  provide: SUPABASE_CLIENT,
  useFactory: (): SupabaseClient => {
    return createClient(
      environment.supabaseUrl,
      environment.supabaseAnonKey
    ) as SupabaseClient
  },
}

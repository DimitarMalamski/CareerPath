import { Provider } from '@angular/core'
import { SUPABASE_CLIENT } from '../supabase-client.token';

console.error('E2E SUPABASE PROVIDER LOADED')

export const supabaseClientE2eProvider: Provider = {
  provide: SUPABASE_CLIENT,
  useValue: {
    auth: {
      signInWithPassword: async () => ({ data: {}, error: null }),
      signUp: async () => ({ data: {}, error: null }),
      signOut: async () => ({}),
      getSession: async () => ({
        data: {
          session: {
            user: {
              id: 'c004a123-226d-4c19-b44e-5b7251f09282',
              email: 'e2e@careerpath.dev',
            },
          },
        },
      }),
    },
  },
}

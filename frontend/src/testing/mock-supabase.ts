import { vi } from 'vitest';
import { SUPABASE_CLIENT } from '../app/core/supabase-client.token';

export const mockSupabaseClient = {
  auth: {
    getSession: vi.fn().mockResolvedValue({
      data: {
        session: {
          user: {id: '123'},
          access_token: 'mock',
          refresh_token: 'mock'
        }
      }
    }),
    signInWithPassword: vi.fn(),
    signOut: vi.fn(),
    onAuthStateChange: vi.fn((callback) => {
      callback('SIGNED_IN', {user: {id: '123'}});
      return {
        data: {subscription: {unsubscribe: vi.fn()}}
      };
    })
  },
  from: vi.fn(() => ({
    select: vi.fn().mockReturnThis(),
    insert: vi.fn().mockReturnThis(),
    update: vi.fn().mockReturnThis(),
    delete: vi.fn().mockReturnThis(),
    eq: vi.fn().mockReturnThis(),
  })),
};

export const provideMockSupabase = () => ({
  provide: SUPABASE_CLIENT,
  useValue: mockSupabaseClient
});

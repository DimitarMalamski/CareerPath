import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { SupabaseService } from '../services/supabase.service';
import {environment} from '../../../environments/environment';

export const authGuard: CanActivateFn = async () => {
  console.log('AUTH GUARD E2E:', environment.e2e)
  if (environment.e2e) {
    return true
  }

  const supabase = inject(SupabaseService);
  const router = inject(Router);

  const session = await supabase.getSession();

  if (!session) {
    await router.navigate(['/auth/login']);
    return false;
  }

  return true;
};

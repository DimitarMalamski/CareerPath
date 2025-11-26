import {
  ApplicationConfig,
  provideBrowserGlobalErrorListeners,
  provideZoneChangeDetection }
from '@angular/core';
import { provideRouter } from '@angular/router';
import {provideHttpClient, withFetch} from '@angular/common/http';
import { SUPABASE_CLIENT } from './core/supabase-client.token';
import { routes } from './app.routes';
import {createClient} from '@supabase/supabase-js';
import {environment} from '../environments/environment';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(withFetch()),
    {
      provide: SUPABASE_CLIENT,
      useFactory: () =>
        createClient(
          environment.supabaseUrl,
          environment.supabaseAnonKey
        )
    }
  ]
};

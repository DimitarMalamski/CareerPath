import {
  APP_INITIALIZER,
  ApplicationConfig,
  provideBrowserGlobalErrorListeners,
  provideZoneChangeDetection
}
  from '@angular/core';
import { provideRouter } from '@angular/router';
import {provideHttpClient, withInterceptors} from '@angular/common/http';
import { jwtInterceptor } from './core/interceptors/jwt.interceptor';
import { SUPABASE_CLIENT } from './core/supabase-client.token';
import { routes } from './app.routes';
import {createClient} from '@supabase/supabase-js';
import {environment} from '../environments/environment';
import {AuthInitService} from './core/services/auth-init.service';
import {supabaseClientE2eProvider} from './core/supabase/supabase-client.e2e.provider';
import {supabaseClientProvider} from './core/supabase-client.provider';

export const appConfig: ApplicationConfig = {
  providers: [
    environment.e2e
      ? supabaseClientE2eProvider
      : supabaseClientProvider,
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(
      withInterceptors([jwtInterceptor])),
    {
      provide: APP_INITIALIZER,
      multi: true,
      deps: [AuthInitService],
      useFactory: (authInit: AuthInitService) => () => authInit.init()
    },

  ]
};

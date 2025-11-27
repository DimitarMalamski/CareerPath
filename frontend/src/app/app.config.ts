import {
  APP_INITIALIZER,
  ApplicationConfig,
  provideBrowserGlobalErrorListeners,
  provideZoneChangeDetection
}
  from '@angular/core';
import { provideRouter } from '@angular/router';
import {provideHttpClient, withFetch, withInterceptors} from '@angular/common/http';
import { jwtInterceptor } from './core/interceptors/jwt.interceptor';
import { SUPABASE_CLIENT } from './core/supabase-client.token';
import { routes } from './app.routes';
import {createClient} from '@supabase/supabase-js';
import {environment} from '../environments/environment';
import {AuthInitService} from './core/services/auth-init.service';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(
      withFetch(),
      withInterceptors([jwtInterceptor])),
    {
      provide: SUPABASE_CLIENT,
      useFactory: () =>
        createClient(
          environment.supabaseUrl,
          environment.supabaseAnonKey
        )
    },

    {
      provide: APP_INITIALIZER,
      multi: true,
      deps: [AuthInitService],
      useFactory: (authInit: AuthInitService) => () => authInit.init()
    }
  ]
};

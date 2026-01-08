import { ApplicationConfig } from '@angular/core'
import { IdentityPort } from './core/testing/identity.port'
import { E2eIdentityService } from './core/testing/e2e-identity.service'

export const appConfigE2E: ApplicationConfig = {
  providers: [
    {
      provide: IdentityPort,
      useClass: E2eIdentityService,
    },
  ],
}

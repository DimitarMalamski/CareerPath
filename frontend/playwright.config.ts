import { defineConfig } from '@playwright/test'
import * as dotenv from 'dotenv'

dotenv.config({ path: '.env.e2e' })

export default defineConfig({
  testDir: './tests/e2e',

  globalSetup: './tests/e2e/setup/auth.setup.ts',

  use: {
    baseURL: process.env.E2E_BASE_URL,
    storageState: 'tests/e2e/.auth/state.json',
  },

  webServer: {
    command: 'npm run start:e2e',
    url: process.env.E2E_BASE_URL,
    reuseExistingServer: true,
  },
})

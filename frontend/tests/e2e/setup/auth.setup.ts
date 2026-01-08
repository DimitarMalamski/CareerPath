import { chromium, FullConfig } from '@playwright/test'
import { createClient } from '@supabase/supabase-js'

async function globalSetup(config: FullConfig) {
  const supabase = createClient(
    process.env.SUPABASE_URL!,
    process.env.SUPABASE_ANON_KEY!
  )

  const { data, error } = await supabase.auth.signInWithPassword({
    email: process.env.E2E_USER_EMAIL!,
    password: process.env.E2E_USER_PASSWORD!,
  })

  if (error || !data.session) {
    throw new Error(`Supabase E2E login failed: ${error?.message}`)
  }

  const browser = await chromium.launch()
  const context = await browser.newContext()

  // Inject session into browser storage
  await context.addInitScript(
    ({ key, session }) => {
      localStorage.setItem(key, JSON.stringify(session))
    },
    {
      key: `sb-${process.env.SUPABASE_PROJECT_REF}-auth-token`,
      session: data.session,
    }
  )

  // Open any page so storage is committed
  const page = await context.newPage()
  await page.goto(process.env.E2E_BASE_URL!)

  // Save authenticated state
  await context.storageState({ path: 'tests/e2e/.auth/state.json' })

  await browser.close()
}

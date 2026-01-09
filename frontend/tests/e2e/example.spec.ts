import { test, expect } from '@playwright/test'

test('authenticated user sees job recommendations', async ({ page }) => {
  await page.goto('/jobs')

  await expect(
    page.getByText('Junior Backend Developer')
  ).toBeVisible()

  await expect(
    page.getByText('Frontend Engineer')
  ).toBeVisible()
})

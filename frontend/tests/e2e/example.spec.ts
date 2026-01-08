import { test, expect } from '@playwright/test'

test('authenticated user can access jobs page', async ({ page }) => {
  await page.goto('/jobs')

  await expect(page).toHaveURL(/\/jobs/)

  await expect(
    page.getByTestId('jobs-title')
  ).toBeAttached()
})

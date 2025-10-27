/// <reference types="node" />

import { defineConfig } from 'vitest/config'
import angular from '@analogjs/vite-plugin-angular'
import { fileURLToPath } from 'url'
import { dirname, resolve } from 'path'

const __filename = fileURLToPath(import.meta.url)
const __dirname = dirname(__filename)

export default defineConfig({
  plugins: [angular()],
  test: {
    globals: true,
    environment: 'jsdom',
    setupFiles: [resolve(__dirname, 'src/test-setup.ts')],
    include: ['src/**/*.spec.ts'],
    coverage: {
      provider: 'v8',
      reportsDirectory: './coverage',
      reporter: ['text', 'lcov', 'json', 'html'],
    },
    typecheck: {
      tsconfig: './tsconfig.vitest.json',
    },
    reporters: [
      'default',
      ['junit', { outputFile: 'test-results.xml' }],
    ],
  },
  resolve: {
    conditions: ['browser', 'module'],
  },
  esbuild: {
    tsconfigRaw: {
      compilerOptions: {
        target: 'es2022',
        useDefineForClassFields: true
      }
    }
  }
})

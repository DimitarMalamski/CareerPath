docker compose -f "../compose/docker-compose.dev.yml" `
  --env-file "../env/.env.dev" `
  up -d --build

Write-Host "Dev environment running at:" -ForegroundColor Green
Write-Host "   Frontend → http://localhost:4200"
Write-Host "   Backend  → http://localhost:8080"
Write-Host "   PgAdmin  → http://localhost:5050 (if enabled)"

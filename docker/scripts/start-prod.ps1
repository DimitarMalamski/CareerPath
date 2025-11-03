Set-Location "$PSScriptRoot/../compose"

docker compose -f "docker-compose.prod.yml" `
  --env-file "../env/.env.prod" `
  up -d --build

Write-Host "Prod environment containers started!" -ForegroundColor Green
Write-Host "   Frontend → http://localhost"
Write-Host "   Backend  → http://localhost:8080"
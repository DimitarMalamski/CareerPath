Write-Host "Stopping CareerPath PROD environment..." -ForegroundColor Red
Set-Location "$PSScriptRoot/../compose"
docker compose -f "docker-compose.prod.yml" down
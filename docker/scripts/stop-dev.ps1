Write-Host "Stopping CareerPath DEV environment..." -ForegroundColor Red
Set-Location "$PSScriptRoot/../compose"
docker compose -f "docker-compose.dev.yml" down
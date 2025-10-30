# ===========================================
#  stop-env.ps1
#  Stops all CareerPath Docker containers,
#  cleans up networks and unused resources,
#  but KEEPS all named volumes (like SonarQube).
# ===========================================

Write-Host "Stopping CareerPath containers..." -ForegroundColor Yellow

# Stop and remove all containers defined in docker-compose.yml
docker compose -f "./compose/docker-compose.yml" down

# Optional: ask to prune only containers/images/networks
$answer = Read-Host "Do you want to clean up dangling containers/images/networks? (y/n)"
if ($answer -eq "y") {
    Write-Host "Cleaning up unused containers, networks, and images (volumes are preserved)..." -ForegroundColor Cyan
    docker system prune -f --volumes=false
}

Write-Host ""
Write-Host "✅ All containers stopped successfully, volumes preserved." -ForegroundColor Green

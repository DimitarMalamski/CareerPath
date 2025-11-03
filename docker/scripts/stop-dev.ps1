# ===========================================
# stop-env.ps1
# Stops all CareerPath Docker containers (dev, prod, sonar),
# cleans up networks and unused resources,
# but KEEPS all named volumes (e.g., databases, SonarQube).
# ===========================================

Write-Host "🛑 Stopping all CareerPath containers..." -ForegroundColor Yellow

# Paths to compose files
$composeFiles = @(
    "./compose/docker-compose.prod.yml",
    "./compose/docker-compose.dev.yml",
    "./compose/docker-compose.prod.yml",
    "./compose/docker-compose.sonar.yml"
)

# Stop each if it exists
foreach ($file in $composeFiles) {
    if (Test-Path $file) {
        Write-Host "Stopping containers from: $file" -ForegroundColor Cyan
        docker compose -f $file down
    }
}

# Also stop any container that starts with 'cp-'
$runningContainers = docker ps --format "{{.Names}}" | Where-Object { $_ -like "cp-*" }
if ($runningContainers) {
    Write-Host "Stopping additional CareerPath containers..." -ForegroundColor Cyan
    $runningContainers | ForEach-Object { docker stop $_; docker rm $_ }
}

Write-Host ""
$answer = Read-Host "Do you want to prune dangling containers/images/networks? (y/n)"
if ($answer -eq "y") {
    Write-Host "🧹 Cleaning up unused containers, networks, and images (volumes preserved)..." -ForegroundColor Cyan
    docker system prune -f --volumes=false
}

Write-Host ""
Write-Host "✅ All CareerPath containers stopped successfully, volumes preserved." -ForegroundColor Green

# ===========================================
#  start-env.ps1
#  Simple helper script to start CareerPath
#  environments with Docker Compose.
# ===========================================

param (
    [Parameter(Mandatory = $true)]
    [ValidateSet("dev", "prod")]
    [string]$envName
)

Write-Host ("Starting CareerPath environment: " + $envName) -ForegroundColor Cyan

# Stop any running containers first (optional cleanup)
docker compose -f "./compose/docker-compose.yml" down

# Set the environment variable for Docker Compose
$env:ENV = $envName

# Build and start in detached mode
docker compose -f "./compose/docker-compose.yml" up -d --build

# Wait a few seconds for containers to initialize
Start-Sleep -Seconds 5

# Show active Spring profile
Write-Host ""
Write-Host "Active Spring Profile:" -ForegroundColor Yellow
docker logs cp-backend | Select-String "profile"

Write-Host ""
Write-Host ("Environment '" + $envName + "' is now running!") -ForegroundColor Green
Write-Host "Use 'docker compose down' to stop everything."

param (
    [Parameter(Mandatory = $false)]
    [ValidateSet("dev", "prod")]
    [string]$envName = "dev"
)

Write-Host ("🚀 Starting CareerPath environment: " + $envName) -ForegroundColor Cyan

# Stop existing containers first
docker compose -f "../compose/docker-compose.yml" -f "../compose/docker-compose.$envName.yml" down

# Set the ENV variable for Spring Boot
$env:ENV = $envName

# Start containers
docker compose -f "../compose/docker-compose.yml" -f "../compose/docker-compose.$envName.yml" up -d --build

Write-Host ""
Write-Host "✅ $envName environment is now running!" -ForegroundColor Green
Write-Host "Frontend: http://localhost:4200"
Write-Host "Backend:  http://localhost:8080"

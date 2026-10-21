# CareerPath Docker Setup

## Quick Commands
- Start Dev: `.\scripts\start-env.ps1 dev`
- Start Prod: `.\scripts\start-env.ps1 prod`
- Stop All: `.\scripts\stop-env.ps1`

## Services
- Backend: http://localhost:8080
- Frontend: http://localhost:4200
- pgAdmin: http://localhost:5050 (login: admin@example.com, password from PGADMIN_DEFAULT_PASSWORD in your env file)
- SonarQube: http://localhost:9000

## Secrets
Passwords are read from `docker/env/.env.*` files, which are not committed. Copy `docker/env/.env.example` to `.env.dev` / `.env.prod` / `.env.local` and fill in your own values. When running the backend from an IDE with the `local` profile, set the `SPRING_DATASOURCE_PASSWORD` environment variable.

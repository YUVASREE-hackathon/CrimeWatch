# Experiment 9 — Docker Compose Java Deployment

## Aim

Run React, Spring Boot, PostgreSQL, and Nginx as one healthy networked stack with persistent data.

## Requirements

Docker Desktop running; host ports 3000, 18080, and 15432 available. Jenkins remains on 8080.

## Commands

PowerShell:

```powershell
Copy-Item .env.example .env
# Replace secrets in .env.
docker compose config
docker compose up -d --build
docker compose ps
docker volume ls --filter name=crimewatch
Invoke-RestMethod http://localhost:18080/actuator/health
```

Persistence demonstration:

```powershell
docker compose exec postgres psql -U crimewatch -d crimewatch -c "select count(*) from crime_reports;"
docker compose down
docker compose up -d
docker compose exec postgres psql -U crimewatch -d crimewatch -c "select count(*) from crime_reports;"
```

## Configuration

Named volume `crimewatch_postgres_data` survives `docker compose down`. Backend listens on container port 8080 and host port 18080. Nginx is the public entry point on host port 3000.

## Execution

Wait for all four health states to report healthy before opening the application.

## Expected output

The count remains unchanged across container recreation, proving persistence. `http://localhost:3000` serves CrimeWatch and health reports `UP`.

## Actual result

Compose configuration validation passed. Image build, container startup, and persistence evidence are recorded only after execution.

## Screenshots

Capture Compose source, build, `docker compose ps`, volume, both row counts, health JSON, and the application.

## Result

Compose topology validated; runtime validation follows image build.


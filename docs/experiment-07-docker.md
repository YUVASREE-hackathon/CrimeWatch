# Experiment 7 — Docker Container Lifecycle

## Aim

Demonstrate image acquisition and the full container lifecycle using an isolated Nginx container.

## Requirements

Docker Desktop running with Linux containers.

## Commands

PowerShell:

```powershell
.\scripts\docker-lifecycle-demo.ps1
docker stop crimewatch-lifecycle-demo
docker start crimewatch-lifecycle-demo
docker restart crimewatch-lifecycle-demo
docker exec crimewatch-lifecycle-demo nginx -v
docker logs crimewatch-lifecycle-demo
docker inspect crimewatch-lifecycle-demo
docker rm -f crimewatch-lifecycle-demo
docker ps -a --filter name=crimewatch-lifecycle-demo
```

## Configuration

The demo uses host port 8099 to avoid application and Jenkins ports.

## Execution

Run commands one at a time so the terminal visibly proves each lifecycle state.

## Expected output

The container appears in `docker ps`, transitions through stopped/running states, executes Nginx, emits logs, exposes inspection metadata, and is finally absent.

## Actual result

Docker 29.6.2 and Compose 5.3.1 are available. Lifecycle execution is prepared in the script; screenshots must show the real run.

## Screenshots

See Experiment 7 manifest entries.

## Result

Ready for reproducible execution.


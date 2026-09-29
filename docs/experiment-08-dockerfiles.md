# Experiment 8 — Dockerfiles and Image Optimization

## Aim

Build technically equivalent standard and optimized application images and compare real sizes and layers.

## Requirements

Docker Desktop and internet access to pull base images.

## Commands

PowerShell:

```powershell
.\scripts\compare-images.ps1
docker image ls "crimewatch/*"
docker history crimewatch/backend:standard
docker history crimewatch/backend:optimized
docker history crimewatch/frontend:standard
docker history crimewatch/frontend:optimized
```

## Configuration

- Backend standard runtime: full JDK on Ubuntu Jammy.
- Backend optimized runtime: non-root Java 17 Alpine JRE.
- Frontend standard: full Node and Nginx images.
- Frontend optimized: Alpine build and Nginx runtime images.
- Both use multi-stage builds, so compilers and sources do not enter runtime images.

## Execution

Build all four images on the same machine and date. Record sizes from `docker image ls`; do not insert estimated values.

## Expected output

Optimized images should generally be smaller, reducing transfer time, storage use, and attack surface. Exact results depend on upstream layers and local architecture.

## Actual result

Dockerfiles and automated comparison script are implemented. Real size capture follows the local build.

## Screenshots

Capture Dockerfiles, build completion, the four-row image table, and representative `docker history` output.

## Result

Ready for measured comparison.


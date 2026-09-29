$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $PSScriptRoot
$docker = (Get-Command docker -ErrorAction SilentlyContinue).Source
if (-not $docker) { $docker = "$env:LOCALAPPDATA\Programs\DockerDesktop\resources\bin\docker.exe" }
Push-Location $root
try {
    & $docker build -f backend/Dockerfile.standard -t crimewatch/backend:standard backend
    & $docker build -f backend/Dockerfile -t crimewatch/backend:optimized backend
    & $docker build -f frontend/Dockerfile.standard -t crimewatch/frontend:standard frontend
    & $docker build -f frontend/Dockerfile -t crimewatch/frontend:optimized frontend
    & $docker image ls 'crimewatch/*' --format 'table {{.Repository}}\t{{.Tag}}\t{{.Size}}\t{{.ID}}'
    Write-Host "`nUse 'docker history crimewatch/backend:standard' and ':optimized' for the layer comparison screenshot."
} finally { Pop-Location }


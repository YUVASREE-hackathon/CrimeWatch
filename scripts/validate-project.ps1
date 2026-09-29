param([switch]$RunTests)
$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $PSScriptRoot
$docker = (Get-Command docker -ErrorAction SilentlyContinue).Source
if (-not $docker) { $docker = "$env:LOCALAPPDATA\Programs\DockerDesktop\resources\bin\docker.exe" }
$maven = (Get-Command mvn -ErrorAction SilentlyContinue).Source
if (-not $maven) { $maven = 'C:\ProgramData\Jenkins\.jenkins\tools\hudson.tasks.Maven_MavenInstallation\Maven_3\bin\mvn.cmd' }

function Pass($message) { Write-Host "[PASS] $message" -ForegroundColor Green }
function Check-Url($url, $label) {
    $response = Invoke-WebRequest -Uri $url -TimeoutSec 15
    if ($response.StatusCode -ne 200) { throw "$label returned HTTP $($response.StatusCode)" }
    Pass "$label reachable at $url"
}

Push-Location $root
try {
    if (-not (Test-Path $docker)) { throw 'Docker CLI not found' }
    & $docker info --format '{{.ServerVersion}}' | Out-Null
    Pass 'Docker engine reachable'
    & $docker compose config --quiet
    Pass 'Docker Compose configuration valid'

    $required = @('crimewatch-postgres','crimewatch-backend','crimewatch-frontend','crimewatch-nginx')
    $running = & $docker ps --format '{{.Names}}'
    foreach ($container in $required) {
        if ($running -notcontains $container) { throw "Container not running: $container" }
        Pass "Container running: $container"
    }

    Check-Url 'http://localhost:18080/actuator/health' 'Backend health'
    Check-Url 'http://localhost:3000/' 'Frontend through Nginx'
    $health = Invoke-RestMethod 'http://localhost:18080/actuator/health'
    if ($health.status -ne 'UP') { throw 'Backend health status is not UP' }
    Pass 'Backend reports UP'

    if ($RunTests) {
        & $maven -q -f backend\pom.xml "-Dmaven.repo.local=$root\.m2-repository" test
        if ($LASTEXITCODE -ne 0) { throw 'Backend tests failed' }
        Pass 'Backend tests passed'
        Push-Location frontend
        try { npm test; if ($LASTEXITCODE -ne 0) { throw 'Frontend tests failed' } } finally { Pop-Location }
        Pass 'Frontend tests passed'
    }
    Write-Host "`nCrimeWatch validation completed successfully." -ForegroundColor Green
} finally { Pop-Location }


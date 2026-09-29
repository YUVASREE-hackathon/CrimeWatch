$ErrorActionPreference = 'Stop'
$docker = (Get-Command docker -ErrorAction SilentlyContinue).Source
if (-not $docker) { $docker = "$env:LOCALAPPDATA\Programs\DockerDesktop\resources\bin\docker.exe" }
Write-Host '1. Pull image'; & $docker pull nginx:1.29-alpine
Write-Host '2. List images'; & $docker images nginx
Write-Host '3. Run container'; & $docker run -d --name crimewatch-lifecycle-demo -p 8099:80 nginx:1.29-alpine
Write-Host '4. Running containers'; & $docker ps --filter name=crimewatch-lifecycle-demo
Write-Host '5. Logs'; & $docker logs crimewatch-lifecycle-demo
Write-Host '6. Inspect'; & $docker inspect --format '{{.Name}} {{.State.Status}} {{.NetworkSettings.Ports}}' crimewatch-lifecycle-demo
Write-Host "Continue manually: docker stop/start/restart/exec, then remove with 'docker rm -f crimewatch-lifecycle-demo'."


$ErrorActionPreference = 'Continue'
$docker = (Get-Command docker -ErrorAction SilentlyContinue).Source
if (-not $docker) { $docker = "$env:LOCALAPPDATA\Programs\DockerDesktop\resources\bin\docker.exe" }
$maven = (Get-Command mvn -ErrorAction SilentlyContinue).Source
if (-not $maven) { $maven = 'C:\ProgramData\Jenkins\.jenkins\tools\hudson.tasks.Maven_MavenInstallation\Maven_3\bin\mvn.cmd' }

$checks = @(
    @{ Name = 'Git'; Command = { git --version } },
    @{ Name = 'Java'; Command = { java --version } },
    @{ Name = 'Maven'; Command = { & $maven --version } },
    @{ Name = 'Node'; Command = { node --version } },
    @{ Name = 'npm'; Command = { npm --version } },
    @{ Name = 'Docker'; Command = { & $docker --version } },
    @{ Name = 'Docker Compose'; Command = { & $docker compose version } },
    @{ Name = 'Ansible (Ubuntu)'; Command = { wsl -d Ubuntu -e ansible --version } }
)

foreach ($check in $checks) {
    Write-Host "`n[$($check.Name)]" -ForegroundColor Cyan
    try { & $check.Command 2>&1 | Select-Object -First 5 } catch { Write-Host "NOT AVAILABLE: $($_.Exception.Message)" -ForegroundColor Red }
}


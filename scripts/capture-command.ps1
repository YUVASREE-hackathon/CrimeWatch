param(
    [Parameter(Mandatory=$true)][string]$Experiment,
    [Parameter(Mandatory=$true)][string]$Name,
    [Parameter(Mandatory=$true)][string]$Command
)
$root = Split-Path -Parent $PSScriptRoot
$outputDir = Join-Path $root "screenshots/$Experiment/command-output"
New-Item -ItemType Directory -Path $outputDir -Force | Out-Null
$output = Join-Path $outputDir "$Name.txt"
"CrimeWatch command evidence`nDate: $(Get-Date -Format o)`nCommand: $Command`n" | Set-Content $output
Invoke-Expression $Command 2>&1 | Tee-Object -FilePath $output -Append
Write-Host "Text evidence saved to $output"
Write-Host 'MANUAL ACTION REQUIRED: keep this readable output visible and capture the terminal window using Snipping Tool.'

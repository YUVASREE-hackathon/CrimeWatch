# Experiment 1 — Git Installation and Local Repository

## Aim

Verify the development toolchain and create a correctly ignored local Git repository for CrimeWatch.

## Requirements

Windows PowerShell 7, Git, Java, Maven, Node/npm, Docker Desktop, Ubuntu WSL, Ansible, and Jenkins.

## Commands

PowerShell:

```powershell
.\scripts\environment-check.ps1
git init
git status
git add .
git status
git commit -m "feat: initialize CrimeWatch full-stack platform"
git log --oneline --decorate -5
```

Ubuntu:

```bash
git --version
ansible --version
docker --version
docker compose version
```

## Configuration

The repository `.gitignore` excludes secrets, Node dependencies, Java targets, reports, logs, IDE files, and runtime volumes. Do not replace the existing global Git identity unless the displayed values are incorrect.

## Execution

Run each command separately with a readable terminal font. Review `git status` before the first commit to ensure `.env`, `node_modules`, `target`, and logs are absent.

## Expected output

Git reports a clean repository after the commit and shows the new commit in `git log`.

## Actual result

Environment verification completed on 2026-09-29. Git 2.55.0, Maven 3.9.16 through Jenkins, Node 24.18.0, npm 11.16.0, Docker 29.6.2, Compose 5.3.1, Ansible Core 2.20.1, and Jenkins 2.573 were found. Repository initialization and commit evidence must reflect the final local execution.

## Screenshots

Use the Experiment 1 entries in `screenshot-manifest.md`. Do not expose the configured email address unnecessarily.

## Result

The environment and repository rules are ready for a reproducible first commit.


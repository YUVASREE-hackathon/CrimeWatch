# Experiment 13 — Integrated DevOps Project

## Aim

Demonstrate the complete CrimeWatch change-to-deployment lifecycle.

## Requirements

Completed GitHub repository, Jenkins Pipeline job, verified webhook or clearly labelled polling fallback, Docker Desktop, Ubuntu Ansible, and healthy local ports.

## Configuration

```text
Git commit → GitHub push → Webhook or verified SCM poll → Jenkins checkout
→ backend/frontend build → automated tests → JUnit report
→ Docker image build → Ansible validation → deployment → health check → CrimeWatch
```

## Execution

1. Start Jenkins and Docker Desktop.
2. In Ubuntu verify `ansible --version` and `docker version`.
3. Commit and push a visible, valid application change.
4. Show the webhook delivery or verified polling log and automatically triggered Jenkins build.
5. Follow each pipeline stage through successful health check.
6. Run `scripts/validate-project.ps1 -RunTests`.
7. Open CrimeWatch; submit a report as citizen.
8. As admin, review and assign the report.
9. As the assigned officer, begin investigation and add a note.
10. As admin, view analytics and audit activity.

## Expected output

One traceable commit produces one tested image set and one healthy deployment. The new report moves through authorized workflow actions and appears in analytics/audit data.

## Actual result

The authenticated GitHub repository and Jenkins Pipeline job are configured. Commit `bd18dba` was pushed to `main`, detected automatically by SCM polling, checked out in Jenkins build #7, built with Maven/npm, tested (9 backend and 4 frontend tests), containerized, validated against the Ansible playbook, deployed as four healthy Compose services, and verified with backend `UP` plus frontend HTTP 200. Ubuntu execution of the Ansible deployment was independently verified with `failed=0`, followed by an idempotent run with `changed=0`. GitHub webhook delivery is not claimed because the lab Jenkins URL is localhost-only.

## Screenshots

Capture the full chain using the Experiment 13 manifest entries. Each image must include enough context to associate it with the same commit/build.

## Result

Verified end to end through the honest localhost polling fallback; webhook-specific evidence needs a publicly reachable Jenkins URL.

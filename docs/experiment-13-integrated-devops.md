# Experiment 13 — Integrated DevOps Project

## Aim

Demonstrate the complete CrimeWatch change-to-deployment lifecycle.

## Requirements

Completed GitHub repository, Jenkins Pipeline job, verified webhook or clearly labelled polling fallback, Docker Desktop, Ubuntu Ansible, and healthy local ports.

## Configuration

```text
Git commit → GitHub push → Webhook → Jenkins checkout
→ backend/frontend build → automated tests → JUnit report
→ Docker image build → Ansible deployment → health check → CrimeWatch
```

## Execution

1. Start Jenkins and Docker Desktop.
2. In Ubuntu verify `ansible --version` and `docker version`.
3. Commit and push a visible, valid application change.
4. Show the webhook delivery and automatically triggered Jenkins build.
5. Follow each pipeline stage through successful health check.
6. Run `scripts/validate-project.ps1 -RunTests`.
7. Open CrimeWatch; submit a report as citizen.
8. As admin, review and assign the report.
9. As the assigned officer, begin investigation and add a note.
10. As admin, view analytics and audit activity.

## Expected output

One traceable commit produces one tested image set and one healthy deployment. The new report moves through authorized workflow actions and appears in analytics/audit data.

## Actual result

Application source, local tests, Docker topology, Jenkinsfile, Ansible roles, and validation scripts are implemented. GitHub/Jenkins UI and webhook evidence require the student's authenticated external environment.

## Screenshots

Capture the full chain using the Experiment 13 manifest entries. Each image must include enough context to associate it with the same commit/build.

## Result

Local engineering implementation complete; external GitHub/Jenkins integration must be executed and evidenced manually.

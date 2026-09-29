# Experiment 10 — Continuous Automated Testing

## Aim

Run backend and frontend tests, publish JUnit reports, and demonstrate that a failure prevents deployment.

## Requirements

Maven Wrapper or Jenkins tool `Maven 3`, Node/npm, and installed frontend packages.

## Commands

PowerShell:

```powershell
cd backend
.\mvnw.cmd test
cd ..\frontend
npm ci
npm test
```

## Configuration

Backend tests use H2 in PostgreSQL compatibility mode and demo seeding disabled. Frontend tests use JSDOM and Testing Library. Jenkins publishes both XML paths.

## Execution

The backend verifies registration, duplicate conflict, invalid login, report creation/retrieval, validation, unauthenticated access, citizen authorization, officer status update, and prohibited citizen status update. Frontend tests verify landing, demo identities, credential selection, and protected-route redirect.

For the controlled failure exercise, temporarily change one assertion on a separate branch, push it, show the failed Test stage and skipped deploy, then revert/fix and push again. Do not retain the broken test on the final branch.

## Expected output

Final run: 9 backend tests and 4 frontend tests pass with zero failures. A deliberately failing test must stop all downstream deployment stages.

## Actual result

On 2026-09-29 the frontend suite passed 4/4. The first backend run found a nullable timeline-schema defect; it was fixed and the second backend run passed 9/9. This is genuine failure-detection and repair evidence.

## Screenshots

Capture both local final test summaries, Jenkins test stage/report, one controlled failed pipeline, and its corrected successful run.

## Result

Local automated tests pass. Jenkins publication remains to be captured after job execution.


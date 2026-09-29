# Manual Actions Still Requiring Your Accounts or UI

CrimeWatch source and local automation can be built automatically. The following actions require your authenticated accounts or Windows UI and must not be presented as completed until you perform and capture them.

## GitHub

1. Create an empty repository named `crimewatch`.
2. Add the displayed HTTPS URL as `origin`.
3. Push `main` and `develop`.
4. Configure branch protection if permitted.
5. Configure and verify the webhook using Experiment 6.

## Jenkins

1. Confirm `Maven 3` under **Manage Jenkins → Tools**.
2. Ensure the Jenkins service can execute `node`, `npm`, `docker`, `wsl`, and `git`.
3. Create the Freestyle job using Experiment 4.
4. Create the Pipeline-from-SCM job using Experiment 5.
5. Add repository credentials without exposing them in screenshots.
6. Run and capture a successful build plus the controlled failure/recovery exercise.

## Docker Desktop

1. Keep the engine running.
2. Keep Ubuntu WSL integration enabled.
3. Verify host ports 3000, 18080, and 15432 are available.

## Screenshot capture

Use `screenshot-manifest.md` from start to finish. The terminal helper can preserve text, but Windows Snipping Tool or the browser must capture genuine GUI evidence.

# Manual Actions and Remaining Optional Evidence

The GitHub repository, `main`/`develop` pushes, Jenkins Pipeline-from-SCM job, automatic SCM polling, successful pipeline, Docker deployment, test publication, and health checks are complete. Only optional policy changes and report screenshots remain manual.

## GitHub

1. Optionally configure branch protection if the organization permits it.
2. To demonstrate a real webhook instead of the verified polling fallback, expose Jenkins through an institution-approved reachable URL and follow Experiment 6. Do not expose localhost with an unapproved tunnel.

## Jenkins

1. Capture Freestyle build #2, its Tests page, polling cause, console success, configuration, and artifacts.
2. Capture Pipeline build #8, its Tests page, polling cause, console success, and Stage View.
3. The public repository needs no Jenkins credential. Add credentials only if the repository is later made private, and never expose them in screenshots.

## Docker Desktop

1. Keep the engine running.
2. Keep Ubuntu WSL integration enabled for interactive Ansible demonstrations. Jenkins itself uses the service-safe containerized validation path documented in Experiment 5.
3. Verify host ports 3000, 18080, and 15432 are available.

## Screenshot capture

Use `screenshot-manifest.md` from start to finish. The terminal helper can preserve text, but Windows Snipping Tool or the browser must capture genuine GUI evidence.

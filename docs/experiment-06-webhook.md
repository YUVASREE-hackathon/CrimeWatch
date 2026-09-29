# Experiment 6 — GitHub Webhook Trigger

## Aim

Trigger the CrimeWatch Jenkins job automatically after a GitHub push.

## Requirements

A GitHub repository and a Jenkins URL reachable from GitHub. `localhost` is not reachable from GitHub.

## Configuration

Jenkins job: enable **GitHub hook trigger for GITScm polling**.

GitHub repository: **Settings → Webhooks → Add webhook**:

- Payload URL: `https://YOUR-REACHABLE-JENKINS/github-webhook/`
- Content type: `application/json`
- Secret: configure the same protected secret in Jenkins if used
- Events: push events only
- Active: enabled

## Execution

Push a small commit to the configured branch, open GitHub webhook deliveries, verify a successful 2xx delivery, and confirm the corresponding Jenkins build cause says GitHub push.

## Expected output

One push produces one successful GitHub delivery and one Jenkins build.

## Actual result

GitHub connectivity and local Jenkins are verified independently, but the webhook is not verified because Jenkins currently uses `localhost:8080`. Use a reachable server or an institution-approved tunnel. Offline fallback: configure **Poll SCM** (for example every five minutes) and label it as polling, not a webhook.

## Screenshots

Capture webhook settings without secrets, a delivery response, the pushed commit, Jenkins build cause, and successful build.

## Result

Manual action required; no webhook success is claimed.

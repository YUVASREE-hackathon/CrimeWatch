# Experiment 6 — GitHub Webhook and Verified Polling Trigger

## Aim

Trigger the CrimeWatch Jenkins job automatically after a GitHub push, using a webhook when Jenkins is reachable or SCM polling for this localhost lab.

## Requirements

A GitHub repository and a Jenkins URL reachable from GitHub. `localhost` is not reachable from GitHub.

## Configuration

Jenkins job: enable **GitHub hook trigger for GITScm polling**. The versioned `Jenkinsfile` also declares `pollSCM('H/2 * * * *')` as the verified localhost fallback.

GitHub repository: **Settings → Webhooks → Add webhook**:

- Payload URL: `https://YOUR-REACHABLE-JENKINS/github-webhook/`
- Content type: `application/json`
- Secret: configure the same protected secret in Jenkins if used
- Events: push events only
- Active: enabled

## Execution

For a publicly reachable Jenkins URL, push a small commit, inspect GitHub webhook deliveries for a 2xx response, and confirm the Jenkins build cause says GitHub push.

For this localhost installation, push a commit to `main`, open **Git Polling Log**, wait for the scheduled poll, and confirm `Changes found` plus a build cause of `Started by an SCM change`.

## Expected output

One push produces one automatic Jenkins build through the configured trigger available to the environment.

## Actual result

The localhost-safe fallback is verified. At 14:24:57 IST Jenkins polled GitHub, detected commit `0f3f5c8`, logged `Changes found`, and automatically started build #6. It repeated the flow for commit `bd18dba`, starting build #7 with cause `Started by an SCM change`; build #7 finished successfully. A GitHub webhook is not claimed because GitHub cannot reach `localhost:8080`. Use a reachable Jenkins server or an institution-approved tunnel when webhook-specific evidence is required.

## Screenshots

Capture the pushed commit, Git Polling Log showing the new revision and `Changes found`, the automatic build cause, and the successful build. Capture webhook settings and a delivery response only after using a reachable Jenkins URL.

## Result

Automatic GitHub-to-Jenkins polling is verified; webhook success remains correctly unclaimed for localhost.

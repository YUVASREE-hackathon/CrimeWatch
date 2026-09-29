# Experiment 12 — Automated Docker Management with Ansible

## Aim

Demonstrate repeatable, idempotent container deployment and verification.

## Requirements

Experiment 11 environment and the complete CrimeWatch workspace.

## Commands

Ubuntu:

```bash
docker compose ps
ansible-playbook -i ansible/inventory ansible/playbook.yml
docker compose ps
ansible-playbook -i ansible/inventory ansible/playbook.yml
```

## Configuration

The role creates network and volume resources only when absent, pulls external images, uses `docker compose up -d --build --remove-orphans --wait` to reconcile changed application containers, and verifies both URLs.

## Execution

Capture containers before deployment, the first recap, containers after deployment, then the second recap. The second run should show mostly `ok` tasks; build output may still report checks depending on Docker cache.

## Expected output

Four healthy containers, backend status `UP` on host port 18080, frontend HTTP 200 on host port 3000, and no failed Ansible tasks.

## Actual result

The executable playbook is implemented. Runtime outcome must be recorded from Ubuntu; no unexecuted success is claimed.

## Screenshots

Capture before/after container state, both Ansible runs, and the working application.

## Result

Ready for idempotency demonstration.


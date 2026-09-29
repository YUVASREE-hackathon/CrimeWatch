# Experiment 11 — Ansible Server and Nginx Configuration

## Aim

Use Ansible to verify or install Docker, prepare protected deployment configuration, and expose CrimeWatch through Nginx.

## Requirements

Ubuntu WSL, Ansible Core 2.20.1, Docker Desktop WSL integration, and sudo access only if Docker packages must be installed.

## Commands

Ubuntu:

```bash
cd '/mnt/c/Users/thira/Crime devops'
ansible --version
ansible-inventory -i ansible/inventory --graph
ansible-playbook -i ansible/inventory ansible/playbook.yml --syntax-check
export CRIMEWATCH_PROJECT_DIR="$PWD"
export POSTGRES_PASSWORD='replace-me'
export JWT_SECRET='replace-with-at-least-32-characters'
ansible-playbook -i ansible/inventory ansible/playbook.yml
curl -I http://localhost:3000/
```

## Configuration

The `docker_host` role checks Docker and installs `docker.io` plus Compose only when absent. The deployment role renders a mode-0600 environment file. Nginx runs as the configured reverse-proxy container.

## Execution

Use `--check` for file/configuration review where applicable, then execute normally. Capture changed/ok task counts and HTTP verification.

## Expected output

The play recap has zero failed tasks; Nginx returns HTTP 200 on host port 3000 and routes API traffic to the backend.

## Actual result

Ansible is installed and the role/playbook structure is present. Full deployment execution must be performed after Docker images build.

## Screenshots

Capture version, inventory, syntax check, play recap, Nginx configuration, and HTTP response.

## Result

Automation prepared; deployment verification pending.


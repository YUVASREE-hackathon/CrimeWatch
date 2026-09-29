# Experiment 5 — Jenkins Declarative Pipeline

## Aim

Execute the repository `Jenkinsfile` as a real build, test, containerization, Ansible-validation, deployment, and health-check pipeline.

## Requirements

Jenkins Maven tool named `Maven 3`; Git, Node/npm, Docker CLI, Docker Compose, and a running Docker Desktop engine visible to the Jenkins Windows service.

## Configuration

The verified job is `CrimeWatch-Pipeline`, configured as **Pipeline → Pipeline script from SCM → Git** with `https://github.com/YUVASREE-hackathon/CrimeWatch.git`, branch `*/main`, and Script Path `Jenkinsfile`. No credentials are required because the repository is public.

The implemented stages are Checkout, Environment, Install Frontend Dependencies, Build Backend, Build Frontend, Automated Tests, Docker Build, Validate Ansible, Deploy, and Health Check. They execute real commands; none are placeholder `echo` stages.

The Windows Jenkins service runs as `LocalSystem`, and WSL intentionally rejects that account. The pipeline therefore builds a pinned Ansible Core 2.20.1 container to validate the real playbook, then deploys with Docker Compose on this Windows agent. Linux agents execute the playbook directly. The same playbook was separately executed twice in Ubuntu, including an idempotent `changed=0` run.

## Execution

Run the pipeline and inspect Stage View. If the Environment stage cannot find Docker or Node, update the Jenkins service PATH and restart Jenkins before retrying.

## Expected output

JUnit reports are published, artifacts archived, containers become healthy, backend returns `UP` on host port 18080, and the reverse proxy returns HTTP 200 on host port 3000.

## Actual result

Build #7 was started automatically by an SCM change on 2026-09-29 and checked out commit `bd18dba`. Maven 3.9.16, Java 21 targeting Java 17, Node 24.18.0, npm 11.16.0, Docker 29.6.2, and Compose 5.3.1 were verified in the Environment stage. The build published 9 backend and 4 frontend tests, validated `ansible/playbook.yml`, built the tagged application images, reconciled four healthy containers, received backend status `UP`, returned frontend HTTP 200, archived artifacts, and finished `SUCCESS`.

## Screenshots

Capture the pipeline definition, Stage View, console output, test tab, artifacts, and final success.

## Result

Verified: Jenkins build #7 completed successfully at 14:31:47 IST on 2026-09-29.


# Experiment 5 — Jenkins Declarative Pipeline

## Aim

Execute the repository `Jenkinsfile` as a real build, test, containerization, Ansible deployment, and health-check pipeline.

## Requirements

Jenkins Maven tool named `Maven_3`; Git, Node/npm, Docker CLI and WSL Ubuntu visible to the Jenkins Windows service.

## Configuration

Create **Pipeline → Pipeline script from SCM → Git**, enter the repository and credentials, select the integration branch, and leave Script Path as `Jenkinsfile`.

The implemented stages are Checkout, Environment, Install Frontend Dependencies, Build Backend, Build Frontend, Automated Tests, Docker Build, Deploy with Ansible, and Health Check. They execute real commands; none are placeholder `echo` stages.

## Execution

Run the pipeline and inspect Stage View. If the Environment stage cannot find Docker or Node, update the Jenkins service PATH and restart Jenkins before retrying.

## Expected output

JUnit reports are published, artifacts archived, containers become healthy, backend returns `UP` on host port 18080, and the reverse proxy returns HTTP 200 on host port 3000.

## Actual result

The `Jenkinsfile` is syntactically prepared and backed by locally passing build/test commands. Authenticated Jenkins job creation and execution remain manual.

## Screenshots

Capture the pipeline definition, Stage View, console output, test tab, artifacts, and final success.

## Result

Pending verified Jenkins execution.


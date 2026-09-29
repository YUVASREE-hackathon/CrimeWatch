# Final Project Report Outline

## Title

CrimeWatch: Real-Time Crime Report Management and Analytics System with an Integrated DevOps Delivery Pipeline

## Abstract

Summarize the reporting problem, the three-role workflow, the full-stack implementation, and the verified Git–Jenkins–Docker–Ansible delivery lifecycle. State clearly that all records are synthetic and results are from a laboratory environment.

## Problem Statement

Explain fragmented incident intake, limited status visibility, weak assignment traceability, and the difficulty of turning report data into operational insight.

## Objectives

- Provide secure, validated incident intake and tracking.
- Support accountable police and admin workflows.
- Deliver useful analytics without using real crime records.
- Demonstrate automated build, test, containerization, deployment, and verification.

## Existing System

Describe manual registers or disconnected basic CRUD applications, limited traceability, no continuous testing, and manual deployment risk. Avoid unsupported claims about a named real system.

## Proposed System

Describe citizen, officer, and admin workspaces; the six-stage lifecycle; notification, evidence, notes, search, analytics, audit, and system-health capabilities.

## System Architecture

Insert the GitHub → Jenkins → build/test → Docker → Ansible → Nginx/backend/database diagram from the README and explain trust boundaries and ports.

## Technologies Used

Use a table covering React/Vite, Spring Boot/Java/Maven, PostgreSQL, Git/GitHub, Jenkins, Docker/Compose, Nginx, Ansible, JUnit/MockMvc, and Vitest.

## Functional Modules

1. Authentication and authorization
2. Citizen incident reporting
3. Police investigation
4. Admin assignment and management
5. Evidence, notes, and status history
6. Notifications
7. Search and filtering
8. Analytics
9. Audit and system health

## Database Design

Include the entities and relationships in `database/schema.sql`. Explain role and officer profiles, report ownership/assignment, one-to-many history/notes/evidence, notifications, and audit entries.

## API Design

Include the endpoint table from the README, HTTP status use, validation, role boundaries, and consistent error format.

## Git Implementation

Use Experiment 1–3 evidence: initialization, ignore rules, remote synchronization, branch strategy, safe conflict, resolution, and graph.

## Jenkins Implementation

Use Freestyle and Pipeline configuration/screenshots. Explain each real stage and the service-level prerequisites.

## Docker Implementation

Describe multi-stage builds, standard/optimized comparison using measured sizes, Compose services, bridge networking, health checks, port mapping, and persistent volumes.

## Continuous Testing

Report actual totals and durations from JUnit. Include the real initially failing schema test, repair, and final passing result; add the controlled CI failure screenshot if demonstrated.

## Ansible Implementation

Explain the inventory, roles, Docker detection/installation, protected environment file, resource creation, Compose reconciliation, HTTP verification, and second-run idempotency.

## Integrated CI/CD Pipeline

Trace one commit SHA from push through webhook delivery, Jenkins build number, test report, image tags, Ansible recap, and health response.

## Screenshots

Import images in the order defined by `screenshot-manifest.md`. Every caption should state the experiment and what is proved.

## Results

Record actual test totals, real image sizes, container health, persistence row counts, Ansible recap, HTTP results, and demonstrated workflows. Never replace missing values with estimates.

## Advantages

Traceable workflow, role separation, consistent validation, reproducible deployment, persistent data, automated quality gate, operational analytics, and extensive lab evidence.

## Limitations

Laboratory security posture, local-only deployment, no real emergency dispatch integration, local evidence storage, no malware scanner, Hibernate update mode rather than production migrations, and external webhook dependency.

## Future Enhancements

TLS, managed secrets, Flyway migrations, object storage, upload scanning, refresh-token rotation, GIS mapping, multilingual accessibility, observability, backups, and reviewed privacy/retention controls.

## Conclusion

Evaluate the success criteria using verified evidence only. Distinguish local passes from externally verified GitHub/Jenkins/webhook actions.


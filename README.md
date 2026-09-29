# CrimeWatch

## Real-Time Crime Report Management System

CrimeWatch is a realistic full-stack DevOps laboratory project for secure incident reporting, police investigation workflows, administrative operations, and crime analytics. It demonstrates Git, GitHub, Jenkins, automated testing, Docker, PostgreSQL, Nginx, and Ansible as one verifiable delivery lifecycle.

> All bundled people, locations, reports, and statistics are synthetic demonstration data. CrimeWatch is a laboratory system, not an emergency service or production-grade public-safety platform.

## What is implemented

- Citizen registration, login, profile, reporting, evidence upload, report tracking, and notifications
- Police workspace for assigned cases, status transitions, investigation notes, evidence, and analytics
- Admin workspace for all reports, officer assignment, priorities, users, categories, analytics, audit logs, and system health
- Governed lifecycle: `SUBMITTED → UNDER_REVIEW → ASSIGNED → UNDER_INVESTIGATION → RESOLVED → CLOSED`
- Search by report ID, title, location, and city; filters for status and priority; sorting and pagination API
- BCrypt password hashing, JWT authentication, role-based backend authorization, protected frontend routes, CORS, validation, and safe API errors
- Ten-table normalized domain model plus role catalogue and officer profiles
- 21 users, 5 officers, 10 categories, and 30 synthetic crime reports generated on first run
- Responsive desktop, tablet, and mobile interface with charts, timelines, tables, dialogs, loading, empty, and error states
- JUnit/MockMvc backend integration tests and Vitest/Testing Library frontend tests with JUnit XML reports
- Multi-stage optimized and standard Dockerfiles for meaningful image-size comparison
- PostgreSQL and evidence volumes, Docker health checks, Nginx reverse proxy, Jenkins pipeline, and idempotent Ansible deployment

## Architecture

```text
Developer → Git → GitHub → Webhook → Jenkins
                                      │
                    Checkout → Build → Test
                                      │
                               Docker images
                                      │
                                   Ansible
                                      │
Browser → Nginx :80 → React frontend  │
                  └→ Spring Boot :8080 ─→ PostgreSQL :5432
                                           │
                                  persistent named volume
```

## Technology stack

| Layer | Technology |
|---|---|
| Frontend | React, Vite, React Router, Recharts, Lucide, Vitest |
| Backend | Java 17 target, Spring Boot 3.5.7, Spring Security, JPA, Validation, Actuator, Maven |
| Data | PostgreSQL 17; H2 only for isolated integration tests |
| Security | BCrypt, JWT, role authorization, CORS, request validation |
| Delivery | Git/GitHub, Jenkins, Docker/Compose, Nginx, Ansible |

## Repository layout

```text
backend/        Spring Boot API, tests, Dockerfiles, Maven Wrapper
frontend/       React application, tests, Dockerfiles
database/       Readable PostgreSQL reference schema
nginx/          Reverse-proxy configuration
ansible/        Inventory, playbook, roles, protected environment template
scripts/        Environment, lifecycle, image comparison, and validation helpers
docs/           Experiments 1–13, command log, screenshot manifest, report outline
screenshots/    Experiment-specific screenshot directories
Jenkinsfile     Executable declarative CI/CD pipeline
docker-compose.yml
```

## Quick start with Docker Desktop

PowerShell:

```powershell
cd 'C:\Users\thira\Crime devops'
Copy-Item .env.example .env
# Edit .env and replace POSTGRES_PASSWORD and JWT_SECRET.
Push-Location backend
.\mvnw.cmd -DskipTests package
Pop-Location
docker compose up -d --build
docker compose ps
Invoke-RestMethod http://localhost:18080/actuator/health
```

Open [http://localhost:3000](http://localhost:3000). Direct backend health is available at [http://localhost:18080/actuator/health](http://localhost:18080/actuator/health). Containers use their conventional internal ports; non-default host ports avoid the Jenkins, Nginx, and PostgreSQL services already present in Ubuntu WSL.

Stop services without deleting data:

```powershell
docker compose down
```

To remove the persistent database too, use `docker compose down -v` only when deliberately resetting all demo data.

## Demo identities

| Role | Email | Password |
|---|---|---|
| Citizen | `citizen@crimewatch.demo` | `Citizen@123` |
| Officer | `officer1@crimewatch.demo` | `Officer@123` |
| Admin | `admin@crimewatch.demo` | `Admin@123` |

These credentials are for local synthetic demo data only.

## Run without Docker

PostgreSQL must already be reachable and the environment variables in `.env.example` must be supplied.

PowerShell backend:

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

PowerShell frontend:

```powershell
cd frontend
npm ci
npm run dev
```

## Automated tests

PowerShell:

```powershell
cd backend
.\mvnw.cmd test

cd ..\frontend
npm ci
npm test
```

Reports are written to `backend/target/surefire-reports/*.xml` and `frontend/reports/junit.xml`. Jenkins publishes both patterns.

## Important REST endpoints

| Method | Endpoint | Access |
|---|---|---|
| POST | `/api/auth/register` | Public |
| POST | `/api/auth/login` | Public |
| GET/POST | `/api/reports` | Authenticated, role-scoped |
| GET | `/api/reports/{publicId}` | Owner, assigned officer, or admin |
| PUT | `/api/reports/{publicId}/status` | Assigned officer or admin |
| PUT | `/api/reports/{publicId}/assign` | Admin |
| POST | `/api/reports/{publicId}/notes` | Assigned officer or admin |
| POST | `/api/reports/{publicId}/evidence` | Authorized case participant |
| GET | `/api/analytics/summary` | Officer or admin |
| GET | `/api/notifications` | Authenticated user |
| GET | `/api/audit` | Admin |
| GET | `/actuator/health` | Public health probe |

Errors use a consistent structure containing timestamp, HTTP status, safe message, path, and optional field-validation messages. Raw stack traces are never returned.

## Jenkins pipeline

The `Jenkinsfile` performs real work in this order:

1. Checkout
2. Verify toolchain
3. Install frontend dependencies
4. Build backend and frontend
5. Run backend and frontend tests in parallel
6. Publish JUnit reports
7. Build Docker images
8. Deploy using Ansible
9. Verify backend and frontend health

Jenkins must have the Maven tool named `Maven_3`, Git, Node/npm, Docker CLI access, and permission to use Docker. On Windows, the Jenkins service also needs access to WSL Ubuntu for the Ansible stage. See `docs/experiment-05-jenkins-pipeline.md`.

## Git and GitHub workflow

Suggested branches:

```text
main
└── develop
    ├── feature/authentication
    ├── feature/reporting
    ├── feature/dashboard
    ├── feature/analytics
    └── feature/notifications
```

`git fetch` downloads remote history without modifying the checked-out branch. `git pull` performs a fetch and then integrates the selected upstream branch. Demonstration commands and the safe conflict exercise are in `docs/experiment-02-sync.md` and `docs/experiment-03-branching.md`.

## Docker architecture and persistence

- `postgres`: PostgreSQL 17 Alpine with `crimewatch_postgres_data`
- `backend`: optimized Java 17 JRE image with actuator health check
- `frontend`: compiled static React assets served by Nginx
- `nginx`: public reverse proxy on host port 3000, container port 80
- `crimewatch_net`: private bridge network shared by all services
- `crimewatch_evidence_data`: persistent evidence uploads

Compose packages the already tested backend JAR with `Dockerfile.runtime`, keeping deployment fast and deterministic. `backend/Dockerfile` remains the fully self-contained multi-stage source build used for comparison and independent builds. Run `scripts/compare-images.ps1` to build standard and optimized variants and display their real sizes. Image-size results depend on the local Docker cache and must be captured after execution, not invented.

## Ansible deployment

Ubuntu/WSL:

```bash
cd '/mnt/c/Users/thira/Crime devops'
export CRIMEWATCH_PROJECT_DIR="$PWD"
export POSTGRES_PASSWORD='replace-me'
export JWT_SECRET='replace-with-at-least-32-characters'
ansible-playbook -i ansible/inventory ansible/playbook.yml
```

The playbook verifies Docker, installs it with apt when absent, creates protected configuration, network and volume resources, pulls external images, reconciles containers, and performs HTTP verification. Running it a second time demonstrates idempotency.

## Final validation

PowerShell:

```powershell
.\scripts\validate-project.ps1 -RunTests
```

Ubuntu:

```bash
./scripts/validate-project.sh
```

## Experiment mapping

| Experiment | Evidence in this repository |
|---|---|
| 1 | Git setup, repository initialization, ignore rules |
| 2 | Remote synchronization and fetch-versus-pull demonstration |
| 3 | Branch strategy and safe merge-conflict exercise |
| 4 | Jenkins Freestyle instructions and real build/test commands |
| 5 | Executable declarative `Jenkinsfile` |
| 6 | GitHub webhook setup, verification, and offline polling fallback |
| 7 | Reproducible Docker lifecycle script |
| 8 | Standard versus optimized application Dockerfiles |
| 9 | Compose deployment, Java container, networks, health checks, volumes |
| 10 | Backend and frontend automated tests with JUnit XML |
| 11 | Ansible environment and Nginx reverse proxy deployment |
| 12 | Idempotent Ansible-managed Docker deployment |
| 13 | Integrated GitHub → Jenkins → test → Docker → Ansible workflow |

## Screenshot policy

No external GUI screenshot is fabricated. `docs/screenshot-manifest.md` gives the exact command, application, expected evidence, and filename for each required capture. Use the provided command helper to save matching text evidence and capture the real PowerShell, Ubuntu, GitHub, Jenkins, Docker Desktop, or application screen.

## Troubleshooting

- **`docker` not found in a new PowerShell window:** restart the terminal or use `C:\Users\thira\AppData\Local\Programs\DockerDesktop\resources\bin\docker.exe`.
- **Ubuntu cannot run Docker:** enable Docker Desktop → Settings → Resources → WSL Integration → Ubuntu.
- **Host port mapping:** Jenkins and Ubuntu services already occupy conventional host ports, so Compose uses `3000→80` for Nginx, `18080→8080` for the backend, and `15432→5432` for PostgreSQL. Container-to-container traffic still uses ports 80, 8080, and 5432.
- **Port 5432 already used:** stop the local PostgreSQL service or change only the host-side mapping.
- **Jenkins cannot find Node or Docker:** update the Windows service environment, restart the Jenkins service, and verify commands in a Jenkins job.
- **Webhook cannot reach localhost:** GitHub cannot call a private localhost address. Use a reachable Jenkins URL or an approved tunnel; for an offline lab use Poll SCM and document it honestly.
- **Reset demo data:** `docker compose down -v` deletes the database volume and is intentionally destructive.

## Security limitations

This lab includes good baseline controls, but it is not production-grade. A real deployment would add managed secrets, TLS, malware scanning for uploads, database migrations, refresh tokens, stronger observability, backups, rate limiting by identity, security review, and applicable legal/privacy controls.

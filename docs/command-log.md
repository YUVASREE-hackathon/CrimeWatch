# CrimeWatch Command Execution Log

This log records commands actually executed during construction. Add subsequent manual GitHub/Jenkins/lab commands as they are performed; do not record expected commands as successful execution.

| Date | Experiment | Terminal | Command | Purpose | Expected result | Actual result |
|---|---:|---|---|---|---|---|
| 2026-09-29 | 1 | PowerShell | `git --version` | Verify Git | Version displayed | Passed: 2.55.0.windows.1 |
| 2026-09-29 | 1 | PowerShell | `java --version` | Verify Java | Runtime displayed | Passed: Oracle JDK 21.0.11; project compiles to Java 17 |
| 2026-09-29 | 1 | PowerShell | Jenkins Maven `mvn.cmd --version` | Locate Maven | Version displayed | Passed: Maven 3.9.16 |
| 2026-09-29 | 1 | PowerShell | `node --version; npm --version` | Verify frontend tools | Versions displayed | Passed: Node 24.18.0, npm 11.16.0 |
| 2026-09-29 | 1 | PowerShell | Docker CLI version/info | Verify engine | Client/server respond | Passed: Docker 29.6.2 |
| 2026-09-29 | 1 | Ubuntu WSL | `ansible --version` | Verify Ansible | Version displayed | Passed: Ansible Core 2.20.1 |
| 2026-09-29 | 1 | Browser/HTTP | `http://localhost:8080/login` | Verify Jenkins | HTTP 200 | Passed: Jenkins 2.573 |
| 2026-09-29 | 1 | PowerShell | HTTPS HEAD request to GitHub | Verify connectivity | HTTP 200 | Passed |
| 2026-09-29 | 10 | PowerShell | Maven backend compile | Compile Java source | Exit 0 | Failed first: missing `AuthenticationException` import; fixed; passed |
| 2026-09-29 | 10 | PowerShell | `npm install --no-audit --no-fund` | Install frontend packages | Dependencies installed | Passed: 143 packages |
| 2026-09-29 | 10 | PowerShell | `npm run build` | Production frontend build | Vite dist generated | Passed; bundle-size warning only |
| 2026-09-29 | 10 | PowerShell | `npm test` | Run UI tests | Four tests pass | Failed first due missing test cleanup; fixed; passed 4/4 |
| 2026-09-29 | 10 | PowerShell | Maven backend tests | Run API integration tests | Nine tests pass | Failed first due non-null initial status origin; fixed; passed 9/9 |
| 2026-09-29 | 9 | PowerShell | `docker compose config --quiet` | Validate topology | Exit 0 | Passed |
| 2026-09-29 | 1 | PowerShell | Maven `wrapper:wrapper` | Add reproducible wrapper | Wrapper generated | Passed: Maven 3.9.16 wrapper |
| 2026-09-29 | 9 | PowerShell | `docker compose up -d --build --wait` | Build and run the integrated stack | Four healthy services | Passed after changing host mappings to 3000, 18080, and 15432 to avoid existing Jenkins/WSL services |
| 2026-09-29 | 9 | HTTP/API | Admin login, reports, analytics, and categories via Nginx | Verify live data flow | Authenticated responses and seeded records | Passed: admin token issued, 30 reports, 10 categories, 6 locations |
| 2026-09-29 | 12 | Ubuntu WSL | `ansible-playbook -i ansible/inventory ansible/playbook.yml --syntax-check` | Validate playbook | No syntax errors | Passed |
| 2026-09-29 | 12 | Ubuntu WSL | `ansible-playbook -i ansible/inventory ansible/playbook.yml` | Deploy and health-check stack | Play recap without failures | Passed: `failed=0`, all four services healthy |
| 2026-09-29 | 12 | Ubuntu WSL | Repeat Ansible deployment | Verify idempotency | No changes required | Passed: `changed=0`, `failed=0` |
| 2026-09-29 | 10 | Browser | Admin sign-in through `http://localhost:3000` | Verify browser authentication and dashboard | Dashboard loads | First attempt exposed missing public CORS origin; configuration fixed and repeat passed with 30-report dashboard |
| 2026-09-29 | 13 | PowerShell | `.\\scripts\\validate-project.ps1 -RunTests` | Run final integrated validation gate | Services, health, and tests pass | Passed: four containers, backend health, 9 backend tests, and 4 frontend tests |
| 2026-09-29 | 2/13 | PowerShell/GitHub | `git push origin main develop` | Publish CrimeWatch source and Jenkins fixes | Remote branches advance | Passed: `main` and `develop` advanced to `bd18dba` |
| 2026-09-29 | 6 | Jenkins Git Polling Log | Scheduled SCM poll | Detect a GitHub push automatically | New revision and `Changes found` | Passed: commits `0f3f5c8` and `bd18dba` were detected; builds #6 and #7 started automatically |
| 2026-09-29 | 5/10/13 | Jenkins | `CrimeWatch-Pipeline` build #7 | Run full CI/CD pipeline | Tests, images, deployment, and health pass | Passed: 9 backend + 4 frontend tests published, Ansible syntax valid, four containers healthy, frontend HTTP 200, `Finished: SUCCESS` |

## Fields for future entries

- **Expected result** describes success criteria before execution.
- **Actual result** must state what really happened, including failures.
- Link or name the matching screenshot when one is captured.


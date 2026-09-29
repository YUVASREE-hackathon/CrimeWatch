# CrimeWatch Screenshot Manifest

Capture only real PowerShell, Ubuntu, GitHub, Jenkins, Docker Desktop, and CrimeWatch browser screens. Use 1920×1080 or higher where possible, browser zoom 90–100%, and a terminal font large enough to read. Crop only unrelated desktop space. Never include passwords, tokens, webhook secrets, email inboxes, or unrelated AI tools.

The **Action** column is the exact command or screen to open. Files marked MANUAL require user interaction; they are not claimed as captured.

| Screenshot | Exp. | Application | Action | What it proves |
|---|---:|---|---|---|
| `01_git_version.png` | 1 | PowerShell | `git --version` | Git installed |
| `02_java_version.png` | 1 | PowerShell | `java --version` | Java available |
| `03_maven_version.png` | 1 | PowerShell | `backend\mvnw.cmd --version` | Reproducible Maven toolchain |
| `04_node_npm_version.png` | 1 | PowerShell | `node --version; npm --version` | Frontend toolchain |
| `05_docker_versions.png` | 1 | PowerShell | `docker --version; docker compose version` | Docker and Compose |
| `06_ansible_version.png` | 1 | Ubuntu | `ansible --version` | Ansible installed |
| `07_jenkins_version.png` | 1 | Jenkins browser | Manage Jenkins → System Information | Jenkins version 2.573 |
| `08_git_config.png` | 1 | PowerShell | `git config --global --list` (hide email if required) | Git identity configured |
| `09_git_init_status.png` | 1 | PowerShell | `git init; git status` | Repository initialized |
| `10_git_add_commit.png` | 1 | PowerShell | First `git add` and `git commit` output | First snapshot created |
| `11_git_remote.png` | 1 | PowerShell | `git remote -v` | GitHub remote configured |
| `12_git_push.png` | 1 | PowerShell | `git push -u origin main` | First push completed |
| `13_github_repository.png` | 1 | GitHub browser | Repository Code page | Remote contains project |
| `14_git_clone.png` | 1 | PowerShell | Clone verification command/output | Repository can be cloned |
| `01_remote_v.png` | 2 | PowerShell | `git remote -v` | Fetch/push URLs |
| `02_log_before_fetch.png` | 2 | PowerShell | `git log --oneline --all --graph` | Initial history |
| `03_fetch.png` | 2 | PowerShell | `git fetch origin` | Remote refs downloaded |
| `04_fetch_diff.png` | 2 | PowerShell | `git diff HEAD..origin/main` | Fetch does not alter working tree |
| `05_pull.png` | 2 | PowerShell | `git pull --ff-only origin main` | Pull integrates changes |
| `06_sync_status.png` | 2 | PowerShell | `git status; git push` | Final synchronization |
| `01_branch_list.png` | 3 | PowerShell | `git branch -a` | Branch inventory |
| `02_feature_switch.png` | 3 | PowerShell | `git switch -c feature/reporting` | Feature branch created |
| `03_feature_commit.png` | 3 | PowerShell | Feature commit output | Isolated work recorded |
| `04_merge.png` | 3 | PowerShell | Merge into `develop` | Feature integrated |
| `05_conflict.png` | 3 | PowerShell | `git merge feature/conflict-demo; git status` | Real safe conflict |
| `06_conflict_markers.png` | 3 | Editor | `docs/conflict-demo.txt` markers | Competing edits visible |
| `07_conflict_resolved.png` | 3 | PowerShell | Resolution commit and graph | Conflict correctly resolved |
| `01_dashboard.png` | 4 | Jenkins browser | Jenkins dashboard | Jenkins operational |
| `02_new_freestyle.png` | 4 | Jenkins browser | New Item Freestyle screen | Job type selected |
| `03_freestyle_scm.png` | 4 | Jenkins browser | Git SCM configuration | Repository connected |
| `04_freestyle_build.png` | 4 | Jenkins browser | Real build commands | Build/test configuration |
| `05_freestyle_trigger.png` | 4 | Jenkins browser | Build trigger settings | Trigger configured |
| `06_freestyle_console.png` | 4 | Jenkins browser | Console output | Commands executed |
| `07_freestyle_success.png` | 4 | Jenkins browser | Successful build page | Freestyle CI passed |
| `01_pipeline_config.png` | 5 | Jenkins browser | Pipeline from SCM config | Jenkinsfile selected |
| `02_jenkinsfile.png` | 5 | Editor/GitHub | `Jenkinsfile` | Real pipeline definition |
| `03_stage_view.png` | 5 | Jenkins browser | Pipeline Stage View | Ordered stages executed |
| `04_pipeline_tests.png` | 5 | Jenkins browser | Test Result page | JUnit reports published |
| `05_pipeline_console.png` | 5 | Jenkins browser | Console deployment/health lines | Deployment verified |
| `06_pipeline_success.png` | 5 | Jenkins browser | Final build status | Pipeline succeeded |
| `01_webhook_settings.png` | 6 | GitHub browser | Webhook page, secret hidden | Hook configured |
| `02_webhook_delivery.png` | 6 | GitHub browser | Recent delivery 2xx | GitHub reached Jenkins |
| `03_webhook_push.png` | 6 | PowerShell | Triggering push output | Source event occurred |
| `04_webhook_build_cause.png` | 6 | Jenkins browser | Build cause | Automatic trigger confirmed |
| `01_docker_pull.png` | 7 | PowerShell | `docker pull nginx:1.29-alpine` | Image downloaded |
| `02_docker_images.png` | 7 | PowerShell | `docker images nginx` | Image stored |
| `03_docker_run_ps.png` | 7 | PowerShell | Demo run and `docker ps` | Container running |
| `04_docker_stop_start.png` | 7 | PowerShell | Stop/start output | Lifecycle control |
| `05_docker_restart_logs.png` | 7 | PowerShell | Restart and logs | Restart/log access |
| `06_docker_exec.png` | 7 | PowerShell | `docker exec ... nginx -v` | Command execution inside container |
| `07_docker_inspect.png` | 7 | PowerShell | Formatted `docker inspect` | Runtime metadata |
| `08_docker_rm.png` | 7 | PowerShell | Removal and `ps -a` | Cleanup completed |
| `01_backend_dockerfiles.png` | 8 | Editor | Standard and optimized backend Dockerfiles | Meaningful bases compared |
| `02_frontend_dockerfiles.png` | 8 | Editor | Standard and optimized frontend Dockerfiles | Multi-stage builds |
| `03_image_builds.png` | 8 | PowerShell | `compare-images.ps1` output | Images built |
| `04_image_sizes.png` | 8 | PowerShell | Four-row image table | Actual size comparison |
| `05_image_history.png` | 8 | PowerShell | Standard/optimized histories | Layer differences |
| `01_compose_config.png` | 9 | Editor | `docker-compose.yml` | Services, network, volumes, health |
| `02_compose_build.png` | 9 | PowerShell | `docker compose up -d --build` | Stack built |
| `03_compose_ps.png` | 9 | PowerShell | `docker compose ps` | Four healthy containers |
| `04_backend_health.png` | 9 | PowerShell | Health request JSON | Java API healthy |
| `05_volume.png` | 9 | PowerShell | `docker volume inspect crimewatch_postgres_data` | Persistent volume exists |
| `06_data_before.png` | 9 | PowerShell | SQL count before recreation | Initial database state |
| `07_data_after.png` | 9 | PowerShell | Same SQL count after recreation | Data persisted |
| `08_application_landing.png` | 9 | Browser | `http://localhost:3000` | Application deployed |
| `01_backend_tests.png` | 10 | PowerShell | `backend\mvnw.cmd test` summary | 9 backend tests pass |
| `02_frontend_tests.png` | 10 | PowerShell | `npm test` summary | 4 frontend tests pass |
| `03_jenkins_test_stage.png` | 10 | Jenkins browser | Automated Tests stage | CI executes tests |
| `04_jenkins_test_report.png` | 10 | Jenkins browser | JUnit report | Totals/duration visible |
| `05_controlled_failure.png` | 10 | Jenkins browser | Deliberately failed assertion build | Failure blocks deployment |
| `06_fixed_success.png` | 10 | Jenkins browser | Corrected next build | Recovery demonstrated |
| `01_ansible_version.png` | 11 | Ubuntu | `ansible --version` | Tool available |
| `02_inventory.png` | 11 | Ubuntu | `ansible-inventory ... --graph` | Target inventory |
| `03_playbook.png` | 11 | Editor | `ansible/playbook.yml` and roles | Automation source |
| `04_syntax_check.png` | 11 | Ubuntu | Playbook syntax check | YAML valid |
| `05_ansible_run.png` | 11 | Ubuntu | First playbook recap | Server configured |
| `06_nginx_verify.png` | 11 | Ubuntu | `curl -I http://localhost:3000` | Web proxy responds |
| `01_containers_before.png` | 12 | Ubuntu | `docker compose ps` before | Pre-deployment state |
| `02_ansible_deploy.png` | 12 | Ubuntu | Deployment play output | Containers reconciled |
| `03_containers_after.png` | 12 | Ubuntu | `docker compose ps` after | Healthy final state |
| `04_ansible_second_run.png` | 12 | Ubuntu | Second play recap | Idempotency evidence |
| `05_ansible_application.png` | 12 | Browser | Deployed CrimeWatch | User-visible outcome |
| `01_integrated_commit.png` | 13 | PowerShell | Final demo commit/push | Delivery begins |
| `02_integrated_webhook.png` | 13 | GitHub browser | Successful delivery | GitHub triggers Jenkins |
| `03_integrated_pipeline.png` | 13 | Jenkins browser | Full green Stage View | Build through health check |
| `04_integrated_containers.png` | 13 | Docker Desktop | CrimeWatch containers | Runtime deployment |
| `05_citizen_dashboard.png` | 13 | Browser | Citizen workspace | Citizen role output |
| `06_report_submission.png` | 13 | Browser | New tracking ID/timeline | Reporting works |
| `07_admin_assignment.png` | 13 | Browser | Officer assignment action | Admin workflow |
| `08_officer_investigation.png` | 13 | Browser | Status and investigation note | Officer workflow |
| `09_admin_analytics.png` | 13 | Browser | Analytics charts | Operational insight |
| `10_final_validation.png` | 13 | PowerShell | `validate-project.ps1 -RunTests` | Final stack/test verification |

## Capture helper

Example:

```powershell
.\scripts\capture-command.ps1 -Experiment 10_testing -Name 01_backend_tests -Command '.\backend\mvnw.cmd test'
```

The helper stores readable text evidence next to the screenshot folder. It cannot create an authentic terminal-window screenshot; capture the visible real terminal manually with Windows Snipping Tool.

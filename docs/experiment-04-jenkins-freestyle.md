# Experiment 4 — Jenkins Freestyle CI Job

## Aim

Configure Jenkins to check out CrimeWatch, build both applications, run real tests, and publish results.

## Requirements

Jenkins 2.573, Git, the global Maven tool `Maven 3`, Node/npm, and a GitHub repository URL.

## Configuration

The verified job is named `CrimeWatch-Freestyle`.

1. Source Code Management → Git → repository URL and credentials if required.
2. Branch specifier → `*/main`.
3. Enable GitHub-hook support and the verified localhost fallback, Poll SCM with `H/2 * * * *`.
4. Add timestamps to the console output.
5. Build Step, Windows batch:

```bat
call "C:\ProgramData\Jenkins\.jenkins\tools\hudson.tasks.Maven_MavenInstallation\Maven_3\bin\mvn.cmd" -B -f backend\pom.xml clean package
if errorlevel 1 exit /b 1
pushd frontend
call npm ci
if errorlevel 1 exit /b 1
call npm test
if errorlevel 1 exit /b 1
call npm run build
if errorlevel 1 exit /b 1
popd
```

6. Post-build Action → Publish JUnit test results:

```text
backend/target/surefire-reports/*.xml,frontend/reports/*.xml
```

7. Archive artifacts: `backend/target/*.jar,frontend/dist/**`.

## Execution

Push a commit to `main`, allow Poll SCM to detect it, then inspect console output, tests, and archived artifacts. A build is successful only if commands exit successfully.

## Expected output

The job checks out Git, executes 9 backend and 4 frontend tests, builds the JAR and frontend assets, and publishes JUnit results.

## Actual result

The authenticated Freestyle job is configured and verified. Build #2 was automatically started by an SCM change, checked out commit `567c249`, ran all 9 backend and 4 frontend tests with zero failures, built `crimewatch-api.jar` and the Vite production bundle, published JUnit results, archived the JAR plus frontend assets, and finished `SUCCESS` at 14:41:46 IST on 2026-09-29.

## Screenshots

Capture the job configuration, build steps, trigger, console, test report, and successful status.

## Result

Verified: Freestyle builds #1 and #2 both completed successfully; #2 used the latest GitHub commit.


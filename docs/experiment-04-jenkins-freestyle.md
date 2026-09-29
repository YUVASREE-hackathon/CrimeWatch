# Experiment 4 — Jenkins Freestyle CI Job

## Aim

Configure Jenkins to check out CrimeWatch, build both applications, run real tests, and publish results.

## Requirements

Jenkins 2.573, Git, the global Maven tool `Maven_3`, Node/npm, and a GitHub repository URL.

## Configuration

In Jenkins select **New Item → Freestyle project** and name it `crimewatch-freestyle`.

1. Source Code Management → Git → repository URL and credentials if required.
2. Branch specifier → `*/develop` or the selected integration branch.
3. Build Environment → add configured Node PATH if the service cannot see `node`.
4. Build Step, Windows batch:

```bat
call mvn -B -f backend\pom.xml clean test package
cd frontend
call npm ci
call npm test
call npm run build
```

5. Post-build Action → Publish JUnit test results:

```text
backend/target/surefire-reports/*.xml,frontend/reports/*.xml
```

6. Archive artifacts: `backend/target/*.jar,frontend/dist/**`.

## Execution

Run **Build Now**, inspect console output, tests, and archived artifacts. A build is successful only if commands exit successfully.

## Expected output

The job checks out Git, executes 9 backend and 4 frontend tests, builds the JAR and frontend assets, and publishes JUnit results.

## Actual result

Jenkins is reachable at `http://localhost:8080`. The project UI configuration remains a manual authenticated action and is not claimed complete.

## Screenshots

Capture the job configuration, build steps, trigger, console, test report, and successful status.

## Result

Pending manual Jenkins UI creation and verified execution.


pipeline {
    agent any

    tools {
        maven 'Maven 3'
    }

    options {
        timestamps()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '20'))
    }

    triggers {
        // GitHub webhooks work when Jenkins has a public URL. Polling is the
        // automatic fallback for this localhost lab installation.
        pollSCM('H/2 * * * *')
    }

    environment {
        COMPOSE_PROJECT_NAME = 'crimewatch'
        IMAGE_TAG = "build-${BUILD_NUMBER}"
        DOCKER_EXE = 'C:\\Users\\thira\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\docker.exe'
        DOCKER_COMPOSE_EXE = 'C:\\Users\\thira\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\docker-compose.exe'
    }

    stages {
        stage('Checkout') {
            steps { checkout scm }
        }

        stage('Environment') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'git --version && java --version && mvn --version && node --version && npm --version && docker version && docker compose version'
                    } else {
                        bat 'git --version && java --version && mvn --version && node --version && npm --version && "%DOCKER_EXE%" version && "%DOCKER_COMPOSE_EXE%" version'
                    }
                }
            }
        }

        stage('Install Frontend Dependencies') {
            steps { dir('frontend') { script { isUnix() ? sh('npm ci') : bat('npm ci') } } }
        }

        stage('Build Backend') {
            steps { dir('backend') { script { isUnix() ? sh('mvn -B -DskipTests package') : bat('mvn -B -DskipTests package') } } }
        }

        stage('Build Frontend') {
            steps { dir('frontend') { script { isUnix() ? sh('npm run build') : bat('npm run build') } } }
        }

        stage('Automated Tests') {
            parallel {
                stage('Backend Tests') {
                    steps { dir('backend') { script { isUnix() ? sh('mvn -B test') : bat('mvn -B test') } } }
                }
                stage('Frontend Tests') {
                    steps { dir('frontend') { script { isUnix() ? sh('npm test') : bat('npm test') } } }
                }
            }
        }

        stage('Docker Build') {
            steps {
                script { isUnix() ? sh('docker compose build --pull') : bat('"%DOCKER_COMPOSE_EXE%" build --pull') }
            }
        }

        stage('Validate Ansible') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'ansible-playbook -i ansible/inventory ansible/playbook.yml --syntax-check'
                    } else {
                        bat '''"%DOCKER_EXE%" build -t crimewatch/ansible-runner:%IMAGE_TAG% -f ansible/Dockerfile ansible
"%DOCKER_EXE%" run --rm --mount "type=bind,source=%WORKSPACE%,target=/workspace" -w /workspace crimewatch/ansible-runner:%IMAGE_TAG% -i ansible/inventory ansible/playbook.yml --syntax-check'''
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'ansible-playbook -i ansible/inventory ansible/playbook.yml -e crimewatch_project_dir=$WORKSPACE'
                    } else {
                        // Windows services run as LocalSystem, and WSL refuses
                        // LocalSystem sessions. Compose is the reliable deploy
                        // adapter after the same Ansible playbook is validated.
                        bat '"%DOCKER_COMPOSE_EXE%" up -d --build --remove-orphans --wait'
                    }
                }
            }
        }

        stage('Health Check') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'curl --fail --retry 12 --retry-delay 5 http://localhost:18080/actuator/health && curl --fail http://localhost:3000/'
                    } else {
                        powershell '$h = Invoke-RestMethod http://localhost:18080/actuator/health; if ($h.status -ne "UP") { throw "Backend unhealthy" }; (Invoke-WebRequest -UseBasicParsing http://localhost:3000/).StatusCode'
                    }
                }
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'backend/target/surefire-reports/*.xml,frontend/reports/*.xml'
            archiveArtifacts allowEmptyArchive: true, artifacts: 'backend/target/*.jar,frontend/dist/**,docs/**'
        }
        failure {
            echo 'Pipeline stopped. Deployment stages after the failed stage were not executed.'
        }
    }
}

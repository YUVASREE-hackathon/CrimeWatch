#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."

pass() { printf '\033[32m[PASS]\033[0m %s\n' "$1"; }
docker info >/dev/null && pass "Docker engine reachable"
docker compose config --quiet && pass "Docker Compose configuration valid"
for container in crimewatch-postgres crimewatch-backend crimewatch-frontend crimewatch-nginx; do
  docker inspect -f '{{.State.Running}}' "$container" | grep -q true
  pass "Container running: $container"
done
curl --fail --silent http://localhost:18080/actuator/health | grep -q '"status":"UP"' && pass "Backend reports UP"
curl --fail --silent http://localhost:3000/ >/dev/null && pass "Frontend reachable through Nginx"
printf '\nCrimeWatch validation completed successfully.\n'


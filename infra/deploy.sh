#!/bin/bash
echo "Preparando o ambiente e ajustando permissões..."
# Garante que o Grafana (usuário 472) conseguirá ler os dashboards independente de quem clonou o repo
chmod -R 755 grafana/dashboards_json

echo "Baixando as imagens mais recentes do Docker Hub..."
docker pull pgomes61/api-gateway:latest
docker pull pgomes61/auth-service:latest
docker pull pgomes61/academic-service:latest
docker pull pgomes61/assignment-service:latest

echo "Atualizando o cluster Swarm..."
WORKING_DIR=$(pwd) docker stack deploy -c docker-stack.yml plataforma-devops

echo "Deploy concluído! Verifique a saúde dos containers com 'docker service ls'"

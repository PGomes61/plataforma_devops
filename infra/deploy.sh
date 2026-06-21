#!/bin/bash
echo "Baixando as imagens mais recentes do Docker Hub..."

docker pull pgomes61/api-gateway:latest
docker pull pgomes61/auth-service:latest
docker pull pgomes61/academic-service:latest
docker pull pgomes61/assignment-service:latest

echo "Atualizando o cluster Swarm..."
docker stack deploy -c docker-stack.yml plataforma-devops

echo "Deploy concluído! Verifique a saúde dos containers com 'docker service ls'"

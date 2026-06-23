# Plataforma de Microsserviços e DevOps

Este repositório contém o código-fonte e a infraestrutura de uma plataforma acadêmica baseada em microsserviços. O projeto foi desenhado para garantir escalabilidade, resiliência e automação ponta a ponta, operando sob orquestração em contêineres.

## Arquitetura do Sistema

A arquitetura foi projetada com foco em isolamento de falhas e segurança de rede:

* Isolamento de Domínios (Database-per-Service): O sistema é dividido em três domínios de negócio (`auth-service`, `academic-service` e `assignment-service`). Cada microsserviço possui sua própria instância isolada de PostgreSQL, e a comunicação Leste-Oeste ocorre de forma segura via rede interna.
* API Gateway e Roteamento: O tráfego externo passa exclusivamente por um Ponto Único de Entrada na porta `8080`. O Gateway atua como proxy reverso, preservando os cabeçalhos originais e garantindo o suporte ao padrão HATEOAS (Nível 3 de Richardson).
* Orquestração e Alta Disponibilidade: O gerenciamento é feito pelo Docker Swarm. O sistema mantém duas réplicas (2/2) de cada microsserviço com balanceamento de carga automático e capacidade de autocura (Self-Healing).
* Observabilidade: A telemetria é coletada nos bastidores pelo Prometheus e consolidada em painéis no Grafana, operando de forma invisível para o ambiente externo.

## Tecnologias Utilizadas

* Backend: Java, Spring Boot, Spring Cloud Gateway
* Banco de Dados: PostgreSQL
* Infraestrutura e Orquestração: Docker, Docker Swarm
* Qualidade e Automação (CI/CD): GitHub Actions, Testcontainers, PMD, Trivy
* Monitoramento: Prometheus, Grafana

## Fluxo de CI/CD

O versionamento segue a abordagem Trunk-Based Development. Qualquer alteração na branch principal aciona um pipeline automatizado no GitHub Actions que realiza as seguintes etapas:

1. Integração Contínua: Compilação e execução de testes de integração contra instâncias reais de banco de dados geradas sob demanda pelo Testcontainers.
2. Qualidade e Segurança: Análise estática de código via PMD e varredura de vulnerabilidades de nível crítico via Trivy.
3. Entrega Contínua: Construção das imagens Docker e publicação no registro de contêineres.
4. Validação End-to-End: Deploy automatizado em um cluster temporário seguido de execução de Smoke Tests para atestar a integridade do roteamento.

## Instruções de Deploy e Operação

### Pré-requisitos
O servidor hospedeiro deve possuir o Docker instalado. A ativação do modo orquestrador é obrigatória:
```bash
docker swarm init
Inicialização
Todos os arquivos de provisionamento estão centralizados no diretório de infraestrutura. 
A partir da raiz do repositório clonado, execute os comandos abaixo:

Bash
# 1. Acesse o diretorio de infraestrutura
cd infra

# 2. Garanta a permissao de execucao dos scripts
chmod +x deploy.sh smoke-test.sh

# 3. Execute o deploy automatizado
./deploy.sh
Validação de Conectividade
Para atestar que o roteamento interno e os contêineres subiram corretamente, utilize o script de testes:

Bash
./smoke-test.sh [http://127.0.0.1:8080](http://127.0.0.1:8080)
Acessos Locais
API Gateway (Ponto de Entrada): http://127.0.0.1:8080

Monitoramento em tempo real (Grafana): http://127.0.0.1:3000

Encerramento e Limpeza
Para interromper a execução do sistema e limpar os recursos virtuais com segurança, execute:

Bash
docker stack rm plataforma-devops
```
## Autor
Paulo Vinícius Holanda Gomes

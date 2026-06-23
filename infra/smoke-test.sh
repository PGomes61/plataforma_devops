#!/bin/bash

# Define a URL do Gateway
GATEWAY_URL=${1:-http://localhost:8080}
MAX_RETRIES=18
RETRY_INTERVAL=10

echo "====================================================="
echo "Iniciando Smoke Tests E2E na Plataforma"
echo "Alvo: $GATEWAY_URL"
echo "====================================================="

echo "Aguardando o provisionamento do API Gateway e da malha Swarm..."
# Polling de conexao TCP
for ((i=1; i<=MAX_RETRIES; i++)); do
  HTTP_STATUS=$(curl -s -o /dev/null -w "%{http_code}" --max-time 5 "$GATEWAY_URL/usuarios")

  if [ "$HTTP_STATUS" -ne 000 ]; then
    echo "Conexao TCP estabelecida com sucesso na tentativa $i."
    break
  fi

  echo "Tentativa $i/$MAX_RETRIES: Gateway indisponivel. Aguardando ${RETRY_INTERVAL}s..."
  sleep $RETRY_INTERVAL

  if [ "$i" -eq "$MAX_RETRIES" ]; then
    echo "Timeout esgotado. O API Gateway nao respondeu apos $((MAX_RETRIES * RETRY_INTERVAL)) segundos."
    exit 1
  fi
done

echo "====================================================="

# Funcao para testar os endpoints
check_endpoint() {
  local ENDPOINT=$1
  local NOME_SERVICO=$2

  echo -n "Testando roteamento para $NOME_SERVICO ($ENDPOINT)... "

  HTTP_STATUS=$(curl -s -o /dev/null -w "%{http_code}" --max-time 5 "$GATEWAY_URL$ENDPOINT")

  # Considera sucesso HTTP 200 (OK) ou 201 (Created)
  if [ "$HTTP_STATUS" -eq 200 ] || [ "$HTTP_STATUS" -eq 201 ]; then
    echo "[SUCESSO] Status: $HTTP_STATUS"
    return 0
  else
    echo "[FALHOU] Status: $HTTP_STATUS"
    return 1
  fi
}

ERROS=0

# Testando a conectividade do Gateway com cada microsservico
check_endpoint "/usuarios" "Auth Service" || ERROS=$((ERROS+1))
check_endpoint "/disciplinas" "Academic Service" || ERROS=$((ERROS+1))
check_endpoint "/atividades" "Assignment Service" || ERROS=$((ERROS+1))

echo "====================================================="
if [ $ERROS -eq 0 ]; then
  echo "Resultado: TODOS OS TESTES PASSARAM."
  echo "A malha do Docker Swarm e o API Gateway estao saudaveis."
  exit 0
else
  echo "Resultado: FALHA EM $ERROS SERVICO(S)."
  echo "Verifique o Gateway ou a inicializacao dos containers."
  exit 1
fi

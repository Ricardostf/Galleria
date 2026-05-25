#!/bin/bash

# Cores para o output do terminal
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}=======================================${NC}"
echo -e "${GREEN}    Iniciando Galleria ERP System      ${NC}"
echo -e "${GREEN}=======================================${NC}"

echo -e "${BLUE}[1/2] Iniciando o Backend (Spring Boot)...${NC}"
cd backend || exit
./mvnw spring-boot:run &
BACKEND_PID=$!
cd ..

# Aguarda alguns segundos para o Spring Boot começar a subir
sleep 5

echo -e "${YELLOW}[2/2] Iniciando o Frontend (Angular)...${NC}"
cd frontend || exit
npm start &
FRONTEND_PID=$!
cd ..

echo -e "\n${GREEN}🚀 Todos os serviços foram iniciados!${NC}"
echo -e "${GREEN}-> Backend rodando em: http://localhost:8080${NC}"
echo -e "${GREEN}-> Frontend rodando em: http://localhost:4200${NC}"
echo -e "${RED}Pressione Ctrl+C para encerrar os dois serviços.${NC}\n"

# Função para matar ambos os processos ao sair
cleanup() {
    echo -e "\n${RED}Encerrando os serviços...${NC}"
    kill $BACKEND_PID 2>/dev/null
    kill $FRONTEND_PID 2>/dev/null
    echo -e "${GREEN}Serviços encerrados com sucesso!${NC}"
    exit 0
}

# Captura o sinal SIGINT (Ctrl+C) e executa a função cleanup
trap cleanup SIGINT

# Aguarda indefinidamente os processos em background
wait $BACKEND_PID $FRONTEND_PID

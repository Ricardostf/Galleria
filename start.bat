@echo off
color 0A
echo =======================================
echo     Iniciando Galleria ERP System      
echo =======================================

echo [1/2] Iniciando o Backend (Spring Boot)...
cd backend
start "Backend (Spring Boot)" cmd /c "mvnw.cmd spring-boot:run"
cd ..

timeout /t 5 /nobreak >nul

echo [2/2] Iniciando o Frontend (Angular)...
cd frontend
start "Frontend (Angular)" cmd /c "npm start"
cd ..

echo.
echo Todos os servicos foram iniciados em novas janelas!
echo - Backend rodando em: http://localhost:8080
echo - Frontend rodando em: http://localhost:4200
echo.
echo Feche as janelas abertas para encerrar os servicos.
pause

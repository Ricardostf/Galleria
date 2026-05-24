# GalleriaBank

Aplicação full-stack desenvolvida para o desafio GalleriaBank.

## Arquitetura e Tecnologias

Este projeto é dividido em duas partes principais:
1. **Backend**: Spring Boot 3, Java 17, Spring Security (JWT), Spring Data JPA, H2 Database (em memória).
2. **Frontend**: Angular 17, PrimeNG, PrimeFlex.

## Decisões Técnicas

- **Clean Code e SOLID**: O código backend foi estruturado em camadas (Controller, Service, Repository, Mapper, DTO e Entity) para garantir a separação de responsabilidades.
- **DTOs**: Utilização de `records` do Java 17 para DTOs (Data Transfer Objects), garantindo imutabilidade e concisão. O pacote `mapper` centraliza as conversões de e para Entidades.
- **Segurança**: Autenticação stateless utilizando JSON Web Tokens (JWT) providos pela biblioteca `java-jwt`. As senhas são armazenadas com hash `BCrypt`.
- **Tratamento Global de Exceções**: Uso de `@RestControllerAdvice` para interceptar e padronizar as respostas de erros (regras de negócio e validações).
- **Banco de Dados**: H2 em memória foi escolhido para simplificar a configuração inicial, recriando as tabelas e dados a cada reinicialização da aplicação.
- **Frontend Moderno**: Uso de Angular 17 com componentes Standalone. O layout utiliza PrimeNG para componentes de UI ricos (Tabelas, Formulários, Dialogs, Cards) e PrimeFlex para um design responsivo e estruturado.
- **Validação de Exclusões**: Foi implementada lógica para bloquear a exclusão de:
  - Usuários inativos.
  - Clientes que já possuam pedidos vinculados.
  - Produtos que já estejam em algum pedido.

## Funcionalidades Implementadas

- **Autenticação**:
  - Login seguro retornando token JWT.
  - Rotas e recursos do backend protegidos por filtro de segurança.
  - Frontend interceptando requisições e anexando o Token.
  
- **Gerenciamento de Usuários (CRUD)**:
  - Listagem, Criação (com hash automático de senha) e Atualização.
  - Exclusão lógica/física (protegida com base no status do usuário).

- **Gerenciamento de Clientes (CRUD)**:
  - Dados como Nome, CPF (validado nativamente pelo hibernate validator e bloqueio de duplicidade), e Telefone.

- **Gerenciamento de Produtos (CRUD)**:
  - Informações de Descrição e Valor.
  
- **Gerenciamento de Pedidos**:
  - Criação de pedido vinculando um Cliente a múltiplos Produtos, com cálculo automático de subtotal e total.
  - Tela de visualização dos detalhes do pedido e seus itens.

## Como Executar

### Pré-requisitos
- Java 17+
- Node.js (versão 18+)
- Maven

### Executando o Backend
1. Navegue até a pasta `backend/`.
2. Execute o comando Maven:
   ```bash
   ./mvnw spring-boot:run
   ```
3. O backend estará disponível em `http://localhost:8080`.
*(Nota: O banco de dados H2 criará um usuário padrão com login: `admin` e senha: `admin`, conforme script configurável se inserido no data.sql)*

### Executando o Frontend
1. Navegue até a pasta `frontend/`.
2. Instale as dependências:
   ```bash
   npm install
   ```
3. Inicie a aplicação Angular:
   ```bash
   npm start
   ```
4. O frontend estará disponível em `http://localhost:4200`.

## Autor
Ricardo
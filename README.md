# GalleriaCRUD

Aplicação full-stack desenvolvida para o desafio GalleriaCRUD.

## Arquitetura e Tecnologias

Este projeto é dividido em duas partes principais:
1. **Backend**: Spring Boot 3, Java 17, Spring Security (JWT), Spring Data JPA, H2 Database (em memória).
2. **Frontend**: Angular 17, PrimeNG, PrimeFlex.

## Decisões Técnicas

- **Clean Code e SOLID**: O código backend foi estruturado em camadas (Controller, Service, Repository, Mapper, DTO e Entity) para garantir a separação de responsabilidades.
- **DTOs**: Utilização de `records` do Java 17 para DTOs (Data Transfer Objects), garantindo imutabilidade e concisão. O pacote `mapper` centraliza as conversões de e para Entidades.
- **Segurança**: Autenticação stateless utilizando JSON Web Tokens (JWT) providos pela biblioteca `java-jwt`. As senhas são armazenadas com hash `BCrypt`. O endpoint de cadastro de usuários (`POST /usuarios`) foi aberto para permitir criação pública.
- **Tratamento Global de Exceções**: Uso de `@RestControllerAdvice` para interceptar e padronizar as respostas de erros (regras de negócio e validações).
- **Banco de Dados**: H2 em memória foi escolhido para simplificar a configuração inicial, recriando as tabelas e dados a cada reinicialização da aplicação. O banco conta com Seeders que inicializam usuários e clientes.
- **Frontend Moderno**: Uso de Angular 17 com componentes Standalone. O layout utiliza PrimeNG para componentes de UI ricos (Máscaras, Tabelas responsivas, Formulários, Dialogs, Cards) e PrimeFlex para design moderno com Glassmorphism.

## Regras de Negócio e API (Endpoints)

Todos os recursos principais expõem os métodos HTTP básicos do padrão RESTful:

### 👤 Usuários (`/usuarios`)
- `POST /usuarios` - Cria um novo usuário (Rota Pública).
- `GET /usuarios` - Lista todos os usuários.
- `GET /usuarios/{id}` - Busca usuário por ID.
- `PUT /usuarios/{id}` - Atualiza os dados (nome, login, senha).
- `DELETE /usuarios/{id}` - Remoção Lógica (O sistema não deleta a linha, apenas define o status como Inativo).

### 👥 Clientes (`/clientes`)
- `POST /clientes` - Cria um novo cliente (A máscara e formato do CPF são aceitos).
- `GET /clientes` - Lista todos os clientes.
- `GET /clientes/{id}` - Busca cliente por ID.
- `PUT /clientes/{id}` - Atualiza dados do cliente.
- `DELETE /clientes/{id}` - Exclusão protegida. O sistema bloqueia a exclusão e lança erro caso o cliente já possua um ou mais pedidos vinculados.

### 📦 Produtos (`/produtos`)
- `POST /produtos` - Cria um produto.
- `GET /produtos` - Lista produtos.
- `GET /produtos/{id}` - Busca produto por ID.
- `PUT /produtos/{id}` - Atualiza os detalhes.
- `DELETE /produtos/{id}` - Exclusão protegida. O sistema bloqueia a exclusão e lança erro se o produto já estiver inserido nos itens de algum pedido.

### 🛒 Pedidos (`/pedidos`)
- `POST /pedidos` - Cria um pedido completo, enviando o `clienteId` e uma lista de `itens` (com seus respectivos `produtoId` e quantidades). O backend calcula os subtotais e totais no servidor para evitar fraudes.
- `GET /pedidos` - Lista os pedidos.
- `GET /pedidos/{id}` - Retorna os detalhes profundos do pedido (incluindo o cliente expandido, a lista de itens com os dados dos produtos e o total geral calculado).

---

## Como testar via Postman (Autenticação Bearer Token)

Para testar as rotas protegidas da API por fora do Front-end (ex: via Postman ou Insomnia), você precisará obter um token de segurança (JWT):

**Passo 1: Fazer Login e Pegar o Token**
1. Faça uma requisição **POST** para `http://localhost:8080/auth/login`.
2. No Body (raw/JSON), envie as credenciais do administrador padrão:
```json
{
    "login": "admin",
    "senha": "admin"
}
```
3. O retorno conterá o seu token (ex: `eyJhbGciOiJIUz...`). Copie-o.

**Passo 2: Usar o Token nas outras requisições**
Para testar métodos protegidos (como `PUT /clientes/1` ou `DELETE /produtos/2`):
1. No Postman, abra a aba **Authorization**.
2. No menu suspenso Type, selecione **Bearer Token**.
3. No campo Token do lado direito, cole o código copiado do Passo 1 e envie a requisição.

---

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
*(Nota: O banco de dados H2 criará um usuário padrão com login: `admin` e senha: `admin` na inicialização, além de popular alguns clientes iniciais).*

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
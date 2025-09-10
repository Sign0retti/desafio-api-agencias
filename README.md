# API Simples - Desafio OAuth2

Esta aplicação oferece autenticação OAuth2 e operações básicas de cadastro e consulta de usuários e agências.

## Funcionalidades

1. **Criar usuário** - `POST /user/create`
2. **Gerar token OAuth2** - `POST /token` (só funciona com senha correta)
3. **Buscar agências** - `GET /desafio/search` (requer token)
4. **Cadastrar agência** - `POST /desafio/cadastrar` (requer token)

---

## Como testar no Insomnia

### 1. Criar usuário

- **Método:** POST
- **URL:** `http://localhost:8080/user/create`
- **Body (JSON):**
  ```json
  {
    "name": "João Silva",
    "email": "joao@example.com",
    "password": "senha123"
  }
  ```
- **Content-Type:** application/json

---

### 2. Gerar token OAuth2

- **Método:** POST
- **URL:** `http://localhost:8080/token`
- **Body (Form URL Encoded):**
  | Key         | Value              |
  |-------------|--------------------|
  | grant_type  | password           |
  | username    | joao@example.com   |
  | password    | senha123           |
  | client_id   | desafio-client     |
  | client_secret | desafio-secret   |
- **Content-Type:** application/x-www-form-urlencoded

- **Resposta esperada:**
  ```json
  {
    "access_token": "eyJhbGciOiJSUzI1NiJ9...",
    "token_type": "Bearer",
    "expires_in": 7200,
    "scope": "read write"
  }
  ```

---

### 3. Buscar agências (com token)

- **Método:** GET
- **URL:** `http://localhost:8080/desafio/search?posX=-23.55&posY=-46.63`
- **Header:**  
  `Authorization: Bearer SEU_TOKEN_AQUI`

---

### 4. Cadastrar agência (com token)

- **Método:** POST
- **URL:** `http://localhost:8080/desafio/cadastrar`
- **Header:**  
  `Authorization: Bearer SEU_TOKEN_AQUI`
- **Body (JSON):**
  ```json
  {
    "name": "Agência Central",
    "posX": -23.55,
    "posY": -46.63
  }
  ```
- **Content-Type:** application/json

---

## Credenciais OAuth2

- **client_id:** `desafio-client`
- **client_secret:** `desafio-secret`
- **grant_type:** `password`

---

## Como executar

```sh
./mvnw spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`

---

## Observações

- Configure o ambiente Insomnia para facilitar o envio do token nas requisições protegidas.
- O banco de dados utilizado é PostgreSQL (veja `docker-compose.yml

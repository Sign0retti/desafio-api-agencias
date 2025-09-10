# API Simples - Desafio OAuth2

Esta é uma aplicação extremamente simples com apenas 4 funcionalidades principais usando OAuth2.

## Funcionalidades

1. **Criar usuário** - POST `/user/create`
2. **Gerar token OAuth2** - POST `/oauth2/token` (só funciona com senha correta)
3. **Buscar agências** - GET `/desafio/search` (requer token)
4. **Cadastrar agência** - POST `/desafio/cadastrar` (requer token)

## Como usar

### 1. Criar usuário
```bash
curl -X POST http://localhost:8080/user/create \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@example.com", 
    "password": "senha123"
  }'
```

### 2. Gerar token OAuth2
```bash
curl -X POST http://localhost:8080/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password&username=joao@example.com&password=senha123&client_id=desafio-client&client_secret=desafio-secret"
```

**Resposta:**
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiJ9...",
  "token_type": "Bearer",
  "expires_in": 7200,
  "scope": "read write"
}
```

### 3. Buscar agências (com token)
```bash
curl -X GET "http://localhost:8080/desafio/search?posX=-23.55&posY=-46.63" \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

### 4. Cadastrar agência (com token)
```bash
curl -X POST http://localhost:8080/desafio/cadastrar \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -d '{
    "name": "Agência Central",
    "posX": -23.55,
    "posY": -46.63
  }'
```

## Credenciais OAuth2
- **client_id:** `desafio-client`
- **client_secret:** `desafio-secret`
- **grant_type:** `password`

## Executar aplicação
```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`

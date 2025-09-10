# 🔥 Guia Completo de Comandos CURL - OAuth2 API

## 🚀 Fluxo Completo de Teste

### **Passo 1: Criar Usuário** 
**🔓 Endpoint Público**

```bash
curl -X POST http://localhost:8080/user/create \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João OAuth2",
    "email": "joao@oauth2.com",
    "password": "senha123"
  }'
```

**Resposta esperada:**
```json
{
  "id": 1,
  "name": "João OAuth2",
  "email": "joao@oauth2.com",
  "password": "$2a$10$..."
}
```

---

### **Passo 2: Obter Token OAuth2 (Client Credentials)**
**🔑 Mais Rápido para Testes**

```bash
curl -X POST http://localhost:8080/oauth2/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -H "Authorization: Basic Y2xpZW50LWFwcDpzZWNyZXQxMjM=" \
  -d "grant_type=client_credentials&scope=read write"
```

**Resposta esperada:**
```json
{
  "access_token": "eyJraWQiOiJjMjIzYmM0Yi1iOGJjLTQ4MTEtOWZkNS0zYTc5ZTM2ZWNkNzciLCJhbGciOiJSUzI1NiJ9...",
  "scope": "read write",
  "token_type": "Bearer",
  "expires_in": 7199
}
```

**💾 SALVE O TOKEN:**
```bash
export TOKEN="eyJraWQiOiJjMjIzYmM0Yi1iOGJjLTQ4MTEtOWZkNS0zYTc5ZTM2ZWNkNzciLCJhbGciOiJSUzI1NiJ9..."
```

---

### **Passo 3: Cadastrar Agência (Protegido)**
**🔒 Requer Token**

```bash
curl -X POST http://localhost:8080/desafio/cadastrar \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Agência Centro OAuth2",
    "posX": "100",
    "posY": "200"
  }'
```

**Resposta esperada:**
```json
{
  "id": 1,
  "name": "Agência Centro OAuth2",
  "posX": 100.0,
  "posY": 200.0
}
```

---

### **Passo 4: Buscar Agências (Protegido + Cache)**
**🔒 Requer Token + 💾 Com Cache**

```bash
curl -X GET "http://localhost:8080/desafio/search?posX=150&posY=250" \
  -H "Authorization: Bearer $TOKEN"
```

**Resposta esperada:**
```json
[
  {
    "id": 1,
    "name": "Agência Centro OAuth2",
    "posX": 100.0,
    "posY": 200.0,
    "distance": 70.71067811865476
  }
]
```

---

## 🔄 Fluxo Authorization Code (Completo)

### **Passo A: Iniciar Authorization Flow**
**🌐 Abrir no Browser**

```bash
# Cole esta URL no browser:
http://localhost:8080/oauth2/authorize?response_type=code&client_id=client-app&redirect_uri=http://localhost:8080/callback&scope=read%20write
```

**O que acontece:**
1. Redireciona para `/login`
2. Você faz login com credenciais criadas
3. Autoriza a aplicação
4. Recebe `authorization_code` no callback

---

### **Passo B: Trocar Code por Token**
**🔄 Após receber o code**

```bash
curl -X POST http://localhost:8080/oauth2/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -H "Authorization: Basic Y2xpZW50LWFwcDpzZWNyZXQxMjM=" \
  -d "grant_type=authorization_code&code=SEU_AUTHORIZATION_CODE_AQUI&redirect_uri=http://localhost:8080/callback"
```

---

## 🌐 Endpoints de Interface

### **Página Inicial**
```bash
curl http://localhost:8080/
```

### **Página de Login**
```bash
curl http://localhost:8080/login
```

### **Informações de Auth**
```bash
curl http://localhost:8080/auth/info
```

---

## 🧪 Comandos de Teste Completos

### **Script Completo de Teste:**

```bash
#!/bin/bash

echo "🚀 === TESTE COMPLETO OAUTH2 API ==="
echo ""

# Passo 1: Criar usuário
echo "📝 1. Criando usuário..."
USER_RESPONSE=$(curl -s -X POST http://localhost:8080/user/create \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Teste OAuth2",
    "email": "teste@oauth2.com",
    "password": "teste123"
  }')
echo "✅ Usuário criado: $USER_RESPONSE"
echo ""

# Passo 2: Obter token
echo "🔑 2. Obtendo token OAuth2..."
TOKEN_RESPONSE=$(curl -s -X POST http://localhost:8080/oauth2/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -H "Authorization: Basic Y2xpZW50LWFwcDpzZWNyZXQxMjM=" \
  -d "grant_type=client_credentials&scope=read write")

TOKEN=$(echo $TOKEN_RESPONSE | grep -o '"access_token":"[^"]*' | cut -d'"' -f4)
echo "✅ Token obtido: ${TOKEN:0:50}..."
echo ""

# Passo 3: Cadastrar agência
echo "🏢 3. Cadastrando agência..."
AGENCY_RESPONSE=$(curl -s -X POST http://localhost:8080/desafio/cadastrar \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Agência Teste CURL",
    "posX": "120",
    "posY": "300"
  }')
echo "✅ Agência criada: $AGENCY_RESPONSE"
echo ""

# Passo 4: Buscar agências
echo "🔍 4. Buscando agências..."
SEARCH_RESPONSE=$(curl -s -X GET "http://localhost:8080/desafio/search?posX=100&posY=200" \
  -H "Authorization: Bearer $TOKEN")
echo "✅ Agências encontradas: $SEARCH_RESPONSE"
echo ""

echo "🎉 TESTE COMPLETO FINALIZADO!"
```

---

## 💡 Comandos Úteis

### **Verificar se aplicação está rodando:**
```bash
curl -I http://localhost:8080/
```

### **Testar sem token (deve dar 401):**
```bash
curl http://localhost:8080/desafio/search?posX=100&posY=200
```

### **Testar com token inválido:**
```bash
curl -H "Authorization: Bearer token_invalido" \
  http://localhost:8080/desafio/search?posX=100&posY=200
```

### **Ver headers de resposta:**
```bash
curl -v http://localhost:8080/oauth2/authorize?response_type=code&client_id=client-app&redirect_uri=http://localhost:8080/callback&scope=read%20write
```

---

## 🔧 Troubleshooting

### **Erro 401 Unauthorized:**
```bash
# Verifique se o token está válido
echo $TOKEN

# Obtenha um novo token
curl -X POST http://localhost:8080/oauth2/token \
  -H "Authorization: Basic Y2xpZW50LWFwcDpzZWNyZXQxMjM=" \
  -d "grant_type=client_credentials&scope=read write"
```

### **Erro Connection Refused:**
```bash
# Verifique se a aplicação está rodando
ps aux | grep java | grep DesafioSantander

# Verifique a porta
ss -tlnp | grep :8080
```

---

## 📊 Resumo dos Endpoints

| Método | Endpoint | Autenticação | Descrição |
|--------|----------|--------------|-----------|
| `POST` | `/user/create` | ❌ Público | Criar usuário |
| `POST` | `/oauth2/token` | 🔑 Basic Auth | Obter token |
| `GET` | `/oauth2/authorize` | 🌐 Browser | Iniciar fluxo |
| `POST` | `/desafio/cadastrar` | 🔒 Bearer Token | Cadastrar agência |
| `GET` | `/desafio/search` | 🔒 Bearer Token | Buscar agências |
| `GET` | `/login` | ❌ Público | Página login |
| `GET` | `/` | ❌ Público | Página inicial |

---

## 🎯 One-Liner para Teste Rápido

```bash
# Teste completo em uma linha
curl -s -X POST http://localhost:8080/user/create -H "Content-Type: application/json" -d '{"name":"Test","email":"test@oauth2.com","password":"test123"}' && echo "" && TOKEN=$(curl -s -X POST http://localhost:8080/oauth2/token -H "Authorization: Basic Y2xpZW50LWFwcDpzZWNyZXQxMjM=" -d "grant_type=client_credentials&scope=read write" | grep -o '"access_token":"[^"]*' | cut -d'"' -f4) && curl -s -X POST http://localhost:8080/desafio/cadastrar -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -d '{"name":"QuickTest","posX":"100","posY":"200"}' && echo "" && curl -s -X GET "http://localhost:8080/desafio/search?posX=90&posY=190" -H "Authorization: Bearer $TOKEN"
```

**🔥 OAuth2 Real Funcionando com CURL!**

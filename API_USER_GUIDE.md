# 🚀 Guia do Usuário - API OAuth2 Santander

## 📋 O que você precisa para usar esta API

### 🔧 Pré-requisitos
- **Aplicação rodando** em `http://localhost:8080`
- **Cliente HTTP** (Insomnia, Postman, curl, etc.)
- **Credenciais OAuth2** (fornecidas abaixo)

### 🔑 Credenciais OAuth2 (Fixas)
```
Client ID: client-app
Client Secret: secret123
Authorization Header: Basic Y2xpZW50LWFwcDpzZWNyZXQxMjM=
Base URL: http://localhost:8080
```

**❗ IMPORTANTE:** Essas credenciais são **as mesmas para todos os usuários**. Elas identificam a **aplicação cliente**, não o usuário individual.

---

## 🎯 Fluxo Completo para Usuário

### **Passo 1: Criar sua Conta de Usuário**
```bash
curl -X POST http://localhost:8080/user/create \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Seu Nome",
    "email": "seu.email@exemplo.com",
    "password": "sua_senha_segura"
  }'
```

**Resposta esperada:**
```json
{
  "id": 1,
  "name": "Seu Nome", 
  "email": "seu.email@exemplo.com",
  "password": "$2a$10$..."
}
```

### **Passo 2: Obter Token de Acesso**
```bash
curl -X POST http://localhost:8080/oauth2/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -H "Authorization: Basic Y2xpZW50LWFwcDpzZWNyZXQxMjM=" \
  -d "grant_type=client_credentials&scope=read write"
```

**Resposta esperada:**
```json
{
  "access_token": "eyJraWQiOiJjMjIz...",
  "scope": "read write",
  "token_type": "Bearer",
  "expires_in": 7199
}
```

**💾 COPIE O ACCESS_TOKEN** - Você vai precisar dele!

### **Passo 3: Usar a API com o Token**

#### **Cadastrar Agência:**
```bash
curl -X POST http://localhost:8080/desafio/cadastrar \
  -H "Authorization: Bearer SEU_ACCESS_TOKEN_AQUI" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Minha Agência",
    "posX": "100",
    "posY": "200"
  }'
```

#### **Buscar Agências:**
```bash
curl -X GET "http://localhost:8080/desafio/search?posX=150&posY=250" \
  -H "Authorization: Bearer SEU_ACCESS_TOKEN_AQUI"
```

---

## 📱 Para Usuários do Insomnia/Postman

### **Configuração de Environment:**
```json
{
  "base_url": "http://localhost:8080",
  "client_basic_auth": "Y2xpZW50LWFwcDpzZWNyZXQxMjM=",
  "access_token": ""
}
```

### **Headers para Obter Token:**
```
Authorization: Basic Y2xpZW50LWFwcDpzZWNyZXQxMjM=
Content-Type: application/x-www-form-urlencoded
```

### **Headers para Usar API:**
```
Authorization: Bearer {{access_token}}
Content-Type: application/json
```

---

## 🌐 Fluxo via Browser (Alternativo)

### **1. Acesse no navegador:**
```
http://localhost:8080/oauth2/authorize?response_type=code&client_id=client-app&redirect_uri=http://localhost:8080/callback&scope=read%20write
```

### **2. Faça login com suas credenciais**
- Use email e senha criados no Passo 1

### **3. Autorize a aplicação**
- Clique em "Autorizar"

### **4. Copie o authorization code**
- Será exibido na página de callback

### **5. Troque code por token:**
```bash
curl -X POST http://localhost:8080/oauth2/token \
  -H "Authorization: Basic Y2xpZW50LWFwcDpzZWNyZXQxMjM=" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=authorization_code&code=SEU_CODE&redirect_uri=http://localhost:8080/callback"
```

---

## ❓ Perguntas Frequentes

### **P: As credenciais OAuth2 são sempre as mesmas?**
**R:** ✅ Sim! `client-app:secret123` são fixas e **iguais para todos**. Elas identificam sua aplicação, não o usuário.

### **P: Preciso criar usuário toda vez?**
**R:** ❌ Não! Crie apenas uma vez. Depois use sempre o mesmo email/senha para login via browser.

### **P: O token expira?**
**R:** ✅ Sim, em 2 horas. Quando expirar, repita o Passo 2 para obter um novo.

### **P: Posso usar o mesmo token em múltiplas requisições?**
**R:** ✅ Sim! Use o mesmo token até ele expirar.

### **P: Como sei se meu token está válido?**
**R:** Teste fazendo uma requisição. Se retornar 401, o token expirou.

---

## 🔐 Explicação Técnica

### **Basic Auth vs Bearer Token:**

1. **Basic Auth** (`Y2xpZW50LWFwcDpzZWNyZXQxMjM=`):
   - Identifica sua **aplicação cliente**
   - Sempre o mesmo para todos
   - Usado apenas para **obter tokens**
   - É `client-app:secret123` em Base64

2. **Bearer Token** (`eyJraWQiOiJjMjIz...`):
   - Token de **acesso às APIs**
   - Único para cada sessão
   - Expira em 2 horas
   - Usado em **todas as requisições protegidas**

### **Níveis de Autenticação:**

```
Usuario Individual (email/senha) 
    ↓
OAuth2 Client (client-app:secret123) 
    ↓
Access Token (Bearer eyJraWQi...)
    ↓  
APIs Protegidas (/desafio/*)
```

---

## 📊 Resumo para Distribução

### **O que enviar para o usuário:**
1. ✅ **URL da API:** `http://localhost:8080`
2. ✅ **Credenciais OAuth2:** `client-app` / `secret123`
3. ✅ **Basic Auth Header:** `Y2xpZW50LWFwcDpzZWNyZXQxMjM=`
4. ✅ **Este guia** com exemplos de uso
5. ✅ **Collection do Insomnia** (opcional)

### **O que o usuário deve fazer:**
1. 🔨 Criar conta pessoal (`POST /user/create`)
2. 🔑 Obter token (`POST /oauth2/token`)
3. 🚀 Usar APIs (`/desafio/*`)

**🎯 Pronto! Com isso qualquer pessoa pode usar sua API OAuth2!**

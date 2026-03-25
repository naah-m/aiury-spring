# Documentacao de Endpoints - Aiury API

## Base da API
- Base URL local: `http://localhost:8080`
- Base path: `/api`
- Content-Type: `application/json`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`

## Convencao de Resposta
- Recursos principais retornam HATEOAS (`_links`) em `GET`, `POST` e `PUT`.
- `DELETE` retorna `204 No Content`.
- Erros seguem estrutura `ApiErrorResponse`.

---

## 1) Usuarios (`/api/usuarios`)

### Endpoints

| Metodo | Rota | Descricao | Status esperados |
|---|---|---|---|
| GET | `/api/usuarios` | Lista usuarios (filtro opcional por cidade) | `200` |
| GET | `/api/usuarios/{id}` | Busca usuario por ID | `200`, `404` |
| POST | `/api/usuarios` | Cria usuario | `201`, `400`, `404` |
| PUT | `/api/usuarios/{id}` | Atualiza usuario | `200`, `400`, `404` |
| DELETE | `/api/usuarios/{id}` | Remove usuario | `204`, `404` |

### Filtros
- `GET /api/usuarios?cidadeId=1`

### Exemplo de payload (POST/PUT)

```json
{
  "nomeReal": "Maria Silva",
  "nomeAnonimo": "LuzInterior",
  "dataNascimento": "15-08-1998",
  "celular": "11999998888",
  "senha": "segredo123",
  "cidadeId": 1
}
```

### Exemplo de resposta (200/201)

```json
{
  "id": 10,
  "nomeReal": "Maria Silva",
  "nomeAnonimo": "LuzInterior",
  "dataNascimento": "1998-08-15",
  "celular": "11999998888",
  "dataCadastro": "2026-03-24",
  "cidadeId": 1,
  "_links": {
    "self": { "href": "http://localhost:8080/api/usuarios/10" },
    "usuarios": { "href": "http://localhost:8080/api/usuarios" },
    "atualizar": { "href": "http://localhost:8080/api/usuarios/10" },
    "excluir": { "href": "http://localhost:8080/api/usuarios/10" },
    "chats": { "href": "http://localhost:8080/api/chats?usuarioId=10" },
    "mensagens-enviadas": { "href": "http://localhost:8080/api/mensagens?remetenteId=10" }
  }
}
```

---

## 2) Ajudantes (`/api/ajudantes`)

### Endpoints

| Metodo | Rota | Descricao | Status esperados |
|---|---|---|---|
| GET | `/api/ajudantes` | Lista ajudantes (filtro opcional por disponibilidade) | `200` |
| GET | `/api/ajudantes/{id}` | Busca ajudante por ID | `200`, `404` |
| POST | `/api/ajudantes` | Cria ajudante | `201`, `400` |
| PUT | `/api/ajudantes/{id}` | Atualiza ajudante | `200`, `400`, `404` |
| DELETE | `/api/ajudantes/{id}` | Remove ajudante | `204`, `404` |

### Filtros
- `GET /api/ajudantes?disponivel=true`

### Exemplo de payload (POST/PUT)

```json
{
  "areaAtuacao": "Escuta ativa",
  "motivacao": "Acolho pessoas em situacoes de crise",
  "disponivel": true,
  "rating": 4.8
}
```

### Exemplo de resposta (200/201)

```json
{
  "id": 3,
  "areaAtuacao": "Escuta ativa",
  "motivacao": "Acolho pessoas em situacoes de crise",
  "disponivel": true,
  "rating": 4.8,
  "_links": {
    "self": { "href": "http://localhost:8080/api/ajudantes/3" },
    "ajudantes": { "href": "http://localhost:8080/api/ajudantes" },
    "atualizar": { "href": "http://localhost:8080/api/ajudantes/3" },
    "excluir": { "href": "http://localhost:8080/api/ajudantes/3" },
    "chats-do-ajudante": { "href": "http://localhost:8080/api/chats?ajudanteId=3" }
  }
}
```

---

## 3) Chats (`/api/chats`)

### Endpoints

| Metodo | Rota | Descricao | Status esperados |
|---|---|---|---|
| GET | `/api/chats` | Lista chats com filtros opcionais | `200` |
| GET | `/api/chats/{id}` | Busca chat por ID | `200`, `404` |
| POST | `/api/chats` | Cria chat | `201`, `400`, `404` |
| PUT | `/api/chats/{id}` | Atualiza chat | `200`, `400`, `404` |
| DELETE | `/api/chats/{id}` | Remove chat | `204`, `404` |

### Filtros
- `GET /api/chats?usuarioId=10`
- `GET /api/chats?ajudanteId=3`
- `GET /api/chats?status=EM_ANDAMENTO`
- `GET /api/chats?usuarioId=10&status=EM_ANDAMENTO`

### Status validos de chat
- `INICIADO`
- `EM_ANDAMENTO`
- `FINALIZADO_USUARIO`
- `FINALIZADO_AJUDANTE`
- `FINALIZADO_SISTEMA`

### Exemplo de payload (POST/PUT)

```json
{
  "usuarioId": 10,
  "ajudanteId": 3,
  "dataInicio": "2026-03-24T14:00:00",
  "dataFim": null,
  "status": "INICIADO"
}
```

### Exemplo de resposta (200/201)

```json
{
  "id": 101,
  "usuarioId": 10,
  "ajudanteId": 3,
  "dataInicio": "2026-03-24T14:00:00",
  "dataFim": null,
  "status": "EM_ANDAMENTO",
  "_links": {
    "self": { "href": "http://localhost:8080/api/chats/101" },
    "chats": { "href": "http://localhost:8080/api/chats" },
    "atualizar": { "href": "http://localhost:8080/api/chats/101" },
    "excluir": { "href": "http://localhost:8080/api/chats/101" },
    "mensagens": { "href": "http://localhost:8080/api/mensagens?chatId=101" },
    "usuario": { "href": "http://localhost:8080/api/usuarios/10" },
    "ajudante": { "href": "http://localhost:8080/api/ajudantes/3" }
  }
}
```

---

## 4) Mensagens (`/api/mensagens`)

### Endpoints

| Metodo | Rota | Descricao | Status esperados |
|---|---|---|---|
| GET | `/api/mensagens` | Lista mensagens com filtros opcionais | `200` |
| GET | `/api/mensagens/{id}` | Busca mensagem por ID | `200`, `404` |
| POST | `/api/mensagens` | Cria mensagem | `201`, `400`, `404` |
| PUT | `/api/mensagens/{id}` | Atualiza mensagem | `200`, `400`, `404` |
| DELETE | `/api/mensagens/{id}` | Remove mensagem | `204`, `404` |

### Filtros
- `GET /api/mensagens?chatId=101`
- `GET /api/mensagens?remetenteId=10`
- `GET /api/mensagens?chatId=101&remetenteId=10`

### Exemplo de payload (POST/PUT)

```json
{
  "chatId": 101,
  "remetenteId": 10,
  "texto": "Obrigado pela escuta.",
  "dataEnvio": "2026-03-24T14:15:00"
}
```

### Exemplo de resposta (200/201)

```json
{
  "id": 700,
  "chatId": 101,
  "remetenteId": 10,
  "texto": "Obrigado pela escuta.",
  "dataEnvio": "2026-03-24T14:15:00",
  "_links": {
    "self": { "href": "http://localhost:8080/api/mensagens/700" },
    "mensagens": { "href": "http://localhost:8080/api/mensagens" },
    "atualizar": { "href": "http://localhost:8080/api/mensagens/700" },
    "excluir": { "href": "http://localhost:8080/api/mensagens/700" },
    "chat": { "href": "http://localhost:8080/api/chats/101" },
    "remetente": { "href": "http://localhost:8080/api/usuarios/10" }
  }
}
```

---

## Erros Padronizados

### 400 - Requisicao invalida

```json
{
  "timestamp": "2026-03-24T17:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Erro de validacao nos campos informados.",
  "path": "/api/usuarios",
  "validationErrors": {
    "dataNascimento": "Formato invalido para data de nascimento. Use DD-MM-AAAA"
  }
}
```

### 404 - Recurso nao encontrado

```json
{
  "timestamp": "2026-03-24T17:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Usuario nao encontrado com ID: 999",
  "path": "/api/usuarios/999",
  "validationErrors": null
}
```

### 409 - Violacao de integridade

```json
{
  "timestamp": "2026-03-24T17:00:00",
  "status": 409,
  "error": "Conflict",
  "message": "Violacao de integridade de dados.",
  "path": "/api/usuarios",
  "validationErrors": null
}
```

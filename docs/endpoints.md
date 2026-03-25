# Documentacao de Endpoints - Aiury API

## 1. Informacoes Gerais
- Base URL local: `http://localhost:8080`
- Base path: `/api`
- Content-Type: `application/json`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`

## 2. Padrao de Resposta
- `POST`, `GET` e `PUT` retornam representacoes HATEOAS com `_links`.
- `DELETE` retorna `204 No Content`.
- Erros retornam `ApiErrorResponse`:
```json
{
  "timestamp": "2026-03-24T17:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Erro de validacao nos campos informados.",
  "path": "/api/usuarios",
  "validationErrors": {
    "nomeReal": "O nome real e obrigatorio"
  }
}
```

---

## 3. Recurso Usuarios

### 3.1 `GET /api/usuarios`
- Descricao: lista usuarios com filtro opcional por cidade.
- Parametros:
  - `cidadeId` (query, opcional, `Long`)
- Sucesso: `200 OK`
- Erros possiveis: `400` (parametro invalido)

### 3.2 `GET /api/usuarios/{id}`
- Descricao: retorna um usuario por identificador.
- Parametros:
  - `id` (path, obrigatorio, `Long`)
- Sucesso: `200 OK`
- Erros possiveis: `404` (usuario nao encontrado)

### 3.3 `POST /api/usuarios`
- Descricao: cria um novo usuario.
- Parametros:
  - Body `UsuarioRequest`
- Exemplo de request:
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
- Exemplo de resposta (`201`):
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
- Erros possiveis:
  - `400` payload invalido
  - `404` cidade nao encontrada
  - `409` violacao de integridade (ex.: celular duplicado)

### 3.4 `PUT /api/usuarios/{id}`
- Descricao: atualiza usuario existente.
- Parametros:
  - `id` (path, obrigatorio)
  - Body `UsuarioRequest`
- Exemplo de request:
```json
{
  "nomeReal": "Maria Silva Atualizada",
  "nomeAnonimo": "LuzInterior",
  "dataNascimento": "15-08-1998",
  "celular": "11999990000",
  "senha": "novaSenha123",
  "cidadeId": 2
}
```
- Sucesso: `200 OK` com mesmo formato de resposta do `POST`.
- Erros possiveis: `400`, `404`, `409`.

### 3.5 `DELETE /api/usuarios/{id}`
- Descricao: remove usuario por ID.
- Parametros:
  - `id` (path, obrigatorio)
- Sucesso: `204 No Content`
- Erros possiveis: `404`, `409`.

---

## 4. Recurso Ajudantes

### 4.1 `GET /api/ajudantes`
- Descricao: lista ajudantes com filtro opcional por disponibilidade.
- Parametros:
  - `disponivel` (query, opcional, `Boolean`)
- Sucesso: `200 OK`
- Erros possiveis: `400` (formato de query invalido)

### 4.2 `GET /api/ajudantes/{id}`
- Descricao: retorna ajudante por ID.
- Parametros:
  - `id` (path, obrigatorio)
- Sucesso: `200 OK`
- Erros possiveis: `404`.

### 4.3 `POST /api/ajudantes`
- Descricao: cria ajudante.
- Parametros:
  - Body `AjudanteRequest`
- Exemplo de request:
```json
{
  "areaAtuacao": "Escuta ativa",
  "motivacao": "Atuo em acolhimento voluntario",
  "disponivel": true,
  "rating": 4.8
}
```
- Exemplo de resposta (`201`):
```json
{
  "id": 3,
  "areaAtuacao": "Escuta ativa",
  "motivacao": "Atuo em acolhimento voluntario",
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
- Erros possiveis: `400`.

### 4.4 `PUT /api/ajudantes/{id}`
- Descricao: atualiza ajudante por ID.
- Parametros:
  - `id` (path, obrigatorio)
  - Body `AjudanteRequest`
- Sucesso: `200 OK`
- Erros possiveis: `400`, `404`.

### 4.5 `DELETE /api/ajudantes/{id}`
- Descricao: remove ajudante por ID.
- Parametros:
  - `id` (path, obrigatorio)
- Sucesso: `204 No Content`
- Erros possiveis: `404`, `409`.

---

## 5. Recurso Chats

### 5.1 `GET /api/chats`
- Descricao: lista chats com filtros opcionais.
- Parametros:
  - `usuarioId` (query, opcional, `Long`)
  - `ajudanteId` (query, opcional, `Long`)
  - `status` (query, opcional, `ChatStatus`)
- Valores validos de `status`:
  - `INICIADO`
  - `EM_ANDAMENTO`
  - `FINALIZADO_USUARIO`
  - `FINALIZADO_AJUDANTE`
  - `FINALIZADO_SISTEMA`
- Sucesso: `200 OK`
- Erros possiveis: `400`.

### 5.2 `GET /api/chats/{id}`
- Descricao: retorna chat por ID.
- Parametros:
  - `id` (path, obrigatorio)
- Sucesso: `200 OK`
- Erros possiveis: `404`.

### 5.3 `POST /api/chats`
- Descricao: cria novo chat.
- Parametros:
  - Body `ChatRequest`
- Exemplo de request:
```json
{
  "usuarioId": 10,
  "ajudanteId": 3,
  "dataInicio": "2026-03-24T14:00:00",
  "dataFim": null,
  "status": "INICIADO"
}
```
- Exemplo de resposta (`201`):
```json
{
  "id": 101,
  "usuarioId": 10,
  "ajudanteId": 3,
  "dataInicio": "2026-03-24T14:00:00",
  "dataFim": null,
  "status": "INICIADO",
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
- Erros possiveis: `400`, `404`.

### 5.4 `PUT /api/chats/{id}`
- Descricao: atualiza chat por ID.
- Parametros:
  - `id` (path, obrigatorio)
  - Body `ChatRequest`
- Sucesso: `200 OK`
- Erros possiveis: `400`, `404`.

### 5.5 `DELETE /api/chats/{id}`
- Descricao: remove chat por ID.
- Parametros:
  - `id` (path, obrigatorio)
- Sucesso: `204 No Content`
- Erros possiveis: `404`.

---

## 6. Recurso Mensagens

### 6.1 `GET /api/mensagens`
- Descricao: lista mensagens com filtros opcionais.
- Parametros:
  - `chatId` (query, opcional, `Long`)
  - `remetenteId` (query, opcional, `Long`)
- Sucesso: `200 OK`
- Erros possiveis: `400`.

### 6.2 `GET /api/mensagens/{id}`
- Descricao: retorna mensagem por ID.
- Parametros:
  - `id` (path, obrigatorio)
- Sucesso: `200 OK`
- Erros possiveis: `404`.

### 6.3 `POST /api/mensagens`
- Descricao: cria mensagem vinculada a chat e remetente.
- Parametros:
  - Body `MensagemRequest`
- Exemplo de request:
```json
{
  "chatId": 101,
  "remetenteId": 10,
  "texto": "Obrigado pela escuta.",
  "dataEnvio": "2026-03-24T14:15:00"
}
```
- Exemplo de resposta (`201`):
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
- Erros possiveis: `400`, `404`.

### 6.4 `PUT /api/mensagens/{id}`
- Descricao: atualiza mensagem por ID.
- Parametros:
  - `id` (path, obrigatorio)
  - Body `MensagemRequest`
- Sucesso: `200 OK`
- Erros possiveis: `400`, `404`.

### 6.5 `DELETE /api/mensagens/{id}`
- Descricao: remove mensagem por ID.
- Parametros:
  - `id` (path, obrigatorio)
- Sucesso: `204 No Content`
- Erros possiveis: `404`.

---

## 7. Resumo de Erros HTTP
| Status | Cenario |
|---|---|
| `400` | Validacao de payload, tipo de parametro invalido, corpo mal formatado |
| `404` | Recurso ou referencia nao encontrado |
| `409` | Violacao de integridade de dados |
| `500` | Falha inesperada no servidor |

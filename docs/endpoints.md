# Endpoints da API Aiury

## 1. Convencoes gerais
- Base URL local: `http://localhost:8080`
- Prefixo da API: `/api`
- Media type: `application/json`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

As respostas `GET`, `POST` e `PUT` retornam representacoes HATEOAS com `_links`.

Estrutura padrao de erro:

```json
{
  "timestamp": "2026-03-25T00:10:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Erro de validacao nos campos informados.",
  "path": "/api/usuarios",
  "validationErrors": {
    "nomeReal": "O nome real e obrigatorio"
  }
}
```

## 2. Resumo executivo de recursos

| Recurso | Base path | Filtros suportados |
|---|---|---|
| Estados | `/api/estados` | `uf` |
| Cidades | `/api/cidades` | `estadoId` |
| Usuarios | `/api/usuarios` | `cidadeId` |
| Ajudantes | `/api/ajudantes` | `disponivel` |
| Chats | `/api/chats` | `usuarioId`, `ajudanteId`, `status` |
| Mensagens | `/api/mensagens` | `chatId`, `remetenteId` |

## 3. Estados

| Metodo | Endpoint | Descricao | Sucesso | Erros |
|---|---|---|---|---|
| `POST` | `/api/estados` | Criar estado | `201` | `400`, `409` |
| `GET` | `/api/estados` | Listar estados | `200` | `400` |
| `GET` | `/api/estados/{id}` | Buscar estado por ID | `200` | `404` |
| `PUT` | `/api/estados/{id}` | Atualizar estado | `200` | `400`, `404`, `409` |
| `DELETE` | `/api/estados/{id}` | Excluir estado | `204` | `404`, `409` |

Exemplo `POST /api/estados`:

```json
{
  "nomeEstado": "Sao Paulo",
  "uf": "SP"
}
```

## 4. Cidades

| Metodo | Endpoint | Descricao | Sucesso | Erros |
|---|---|---|---|---|
| `POST` | `/api/cidades` | Criar cidade | `201` | `400`, `404`, `409` |
| `GET` | `/api/cidades` | Listar cidades | `200` | `404` (estado inexistente no filtro) |
| `GET` | `/api/cidades/{id}` | Buscar cidade por ID | `200` | `404` |
| `PUT` | `/api/cidades/{id}` | Atualizar cidade | `200` | `400`, `404`, `409` |
| `DELETE` | `/api/cidades/{id}` | Excluir cidade | `204` | `404`, `409` |

Exemplo `POST /api/cidades`:

```json
{
  "nomeCidade": "Sao Paulo",
  "estadoId": 1
}
```

## 5. Usuarios

| Metodo | Endpoint | Descricao | Sucesso | Erros |
|---|---|---|---|---|
| `POST` | `/api/usuarios` | Criar usuario | `201` | `400`, `404`, `409` |
| `GET` | `/api/usuarios` | Listar usuarios | `200` | `400` |
| `GET` | `/api/usuarios/{id}` | Buscar usuario por ID | `200` | `404` |
| `PUT` | `/api/usuarios/{id}` | Atualizar usuario | `200` | `400`, `404`, `409` |
| `DELETE` | `/api/usuarios/{id}` | Excluir usuario | `204` | `404`, `409` |

Exemplo `POST /api/usuarios`:

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

## 6. Ajudantes

| Metodo | Endpoint | Descricao | Sucesso | Erros |
|---|---|---|---|---|
| `POST` | `/api/ajudantes` | Criar ajudante | `201` | `400` |
| `GET` | `/api/ajudantes` | Listar ajudantes | `200` | `400` |
| `GET` | `/api/ajudantes/{id}` | Buscar ajudante por ID | `200` | `404` |
| `PUT` | `/api/ajudantes/{id}` | Atualizar ajudante | `200` | `400`, `404` |
| `DELETE` | `/api/ajudantes/{id}` | Excluir ajudante | `204` | `404`, `409` |

Exemplo `POST /api/ajudantes`:

```json
{
  "areaAtuacao": "Escuta ativa",
  "motivacao": "Acolhimento voluntario em plantao",
  "disponivel": true,
  "rating": 4.8
}
```

## 7. Chats

| Metodo | Endpoint | Descricao | Sucesso | Erros |
|---|---|---|---|---|
| `POST` | `/api/chats` | Criar chat | `201` | `400`, `404` |
| `GET` | `/api/chats` | Listar chats | `200` | `400` |
| `GET` | `/api/chats/{id}` | Buscar chat por ID | `200` | `404` |
| `PUT` | `/api/chats/{id}` | Atualizar chat | `200` | `400`, `404` |
| `DELETE` | `/api/chats/{id}` | Excluir chat | `204` | `404` |

Valores validos de `status`:
- `INICIADO`
- `EM_ANDAMENTO`
- `FINALIZADO_USUARIO`
- `FINALIZADO_AJUDANTE`
- `FINALIZADO_SISTEMA`

Exemplo `POST /api/chats`:

```json
{
  "usuarioId": 10,
  "ajudanteId": 3,
  "dataInicio": "2026-03-24T14:00:00",
  "dataFim": null,
  "status": "INICIADO"
}
```

Regra adicional de negocio:
- `dataFim` nao pode ser anterior a `dataInicio`.
- status finalizado exige `dataFim`.

## 8. Mensagens

| Metodo | Endpoint | Descricao | Sucesso | Erros |
|---|---|---|---|---|
| `POST` | `/api/mensagens` | Criar mensagem | `201` | `400`, `404` |
| `GET` | `/api/mensagens` | Listar mensagens | `200` | `400` |
| `GET` | `/api/mensagens/{id}` | Buscar mensagem por ID | `200` | `404` |
| `PUT` | `/api/mensagens/{id}` | Atualizar mensagem | `200` | `400`, `404` |
| `DELETE` | `/api/mensagens/{id}` | Excluir mensagem | `204` | `404` |

Exemplo `POST /api/mensagens`:

```json
{
  "chatId": 101,
  "remetenteId": 10,
  "texto": "Obrigado pela escuta de hoje.",
  "dataEnvio": "2026-03-24T14:15:00"
}
```

Regras adicionais de negocio:
- `remetenteId` deve pertencer ao usuario dono do chat.
- `dataEnvio` deve estar entre inicio e fim do chat.

## 9. Status HTTP usados na API

| Status | Uso |
|---|---|
| `200` | Consulta e atualizacao bem-sucedidas |
| `201` | Criacao bem-sucedida |
| `204` | Exclusao bem-sucedida |
| `400` | Validacao ou regra de entrada invalida |
| `404` | Recurso nao encontrado |
| `409` | Conflito de integridade ou unicidade |
| `500` | Erro inesperado no servidor |

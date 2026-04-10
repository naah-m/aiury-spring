# Endpoints da API Aiury

## 1. Convencoes gerais
- Base URL local: `http://localhost:8080`
- Prefixo da API: `/api`
- Media type: `application/json`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- Formato de data (`LocalDate`): `dd/MM/yyyy`
- Formato de data/hora (`LocalDateTime`): `dd/MM/yyyy HH:mm:ss`

As respostas `GET`, `POST` e `PUT` retornam representacoes HATEOAS com `_links`.

Relacoes (`rel`) padronizadas na API:
- Ações: `criar`, `atualizar`, `excluir`
- Colecoes: `usuarios`, `ajudantes`, `chats`, `mensagens`, `cidades`, `estados`
- Relacionamentos: `usuario`, `ajudante`, `remetente`, `chat`, `cidade`, `estado`

Estrutura padrao de erro:

```json
{
  "timestamp": "25/03/2026 00:10:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Erro de validação nos campos informados.",
  "path": "/api/usuarios",
  "validationErrors": {
    "nomeReal": "O nome real e obrigatório"
  }
}
```

Exemplo de erro de formato de data:

```json
{
  "timestamp": "25/03/2026 00:10:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Formato de data/hora inválido no corpo da requisição.",
  "path": "/api/chats",
  "validationErrors": {
    "dataInicio": "Formato de data/hora inválido. Use dd/MM/yyyy HH:mm:ss."
  }
}
```

## 2. Autenticacao e autorizacao
- A API usa a mesma sessao autenticada da aplicacao web (cookie `JSESSIONID`).
- Credenciais persistidas em BCrypt; nao ha suporte a senha em texto puro.
- Para testar endpoints protegidos no navegador:
  1. autentique-se em `http://localhost:8080/login`;
  2. abra o Swagger em `http://localhost:8080/swagger-ui.html`.
- Para testes via Postman, autentique-se antes ou use requests permitidas para o perfil.

Matriz de acesso por perfil:

| Recurso | GET | POST | PUT | DELETE |
|---|---|---|---|---|
| `/api` | ADMIN, USUARIO, AJUDANTE | - | - | - |
| `/api/estados` | ADMIN, USUARIO, AJUDANTE | ADMIN | ADMIN | ADMIN |
| `/api/cidades` | ADMIN, USUARIO, AJUDANTE | ADMIN | ADMIN | ADMIN |
| `/api/usuarios` | ADMIN | ADMIN | ADMIN | ADMIN |
| `/api/ajudantes` | ADMIN | ADMIN | ADMIN | ADMIN |
| `/api/chats` | ADMIN, USUARIO, AJUDANTE | ADMIN | ADMIN | ADMIN |
| `/api/mensagens` | ADMIN, USUARIO, AJUDANTE | ADMIN, USUARIO, AJUDANTE | ADMIN | ADMIN |

## 3. Fluxo recomendado para testes
1. Criar estado
2. Criar cidade com `estadoId` existente
3. Criar usuario com `cidadeId` existente
4. Criar ajudante
5. Criar chat com `usuarioId` e `ajudanteId` existentes
6. Criar mensagem com `chatId` existente e exatamente um remetente (`remetenteId` ou `remetenteAjudanteId`)

## 4. Resumo executivo de recursos

| Recurso | Base path | Filtros suportados |
|---|---|---|
| Root | `/api` | nenhum |
| Estados | `/api/estados` | `uf` |
| Cidades | `/api/cidades` | `estadoId` |
| Usuarios | `/api/usuarios` | `cidadeId` |
| Ajudantes | `/api/ajudantes` | `disponivel` |
| Chats | `/api/chats` | `usuarioId`, `ajudanteId`, `status` |
| Mensagens | `/api/mensagens` | `chatId`, `remetenteId` |

## 5. Root (entrypoint HATEOAS)

| Metodo | Endpoint | Descricao | Sucesso | Erros |
|---|---|---|---|---|
| `GET` | `/api` | Retorna links de navegação para os recursos principais | `200` | - |

Exemplo de relacoes retornadas:
- `self`
- `usuarios`
- `ajudantes`
- `chats`
- `mensagens`
- `cidades`
- `estados`

## 6. Estados

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

## 7. Cidades

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

Pre-requisito:
- `estadoId` deve existir.

## 8. Usuarios

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
  "dataNascimento": "15/08/1998",
  "celular": "11999998888",
  "senha": "segredo123",
  "cidadeId": 1
}
```

Pre-requisito:
- `cidadeId` deve existir.

## 9. Ajudantes

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
  "motivação": "Acolhimento voluntario em plantao",
  "disponivel": true,
  "rating": 4.8
}
```

## 10. Chats

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
  "dataInicio": "25/03/2026 14:00:00",
  "dataFim": null,
  "status": "INICIADO"
}
```

Regra adicional de negocio:
- `dataFim` não pode ser anterior a `dataInicio`.
- status finalizado exige `dataFim`.

Pre-requisitos:
- `usuarioId` deve existir.
- `ajudanteId` deve existir.

## 11. Mensagens

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
  "remetenteAjudanteId": null,
  "texto": "Obrigado pela escuta de hoje.",
  "dataEnvio": "25/03/2026 14:15:00"
}
```

Regras adicionais de negocio:
- informar exatamente um remetente (`remetenteId` ou `remetenteAjudanteId`).
- o remetente informado deve pertencer ao contexto do chat.
- `dataEnvio` deve estar entre início e fim do chat.

Pre-requisitos:
- `chatId` deve existir.
- `remetenteId` ou `remetenteAjudanteId` deve existir e corresponder ao participante do chat.

## 12. Status HTTP usados na API

| Status | Uso |
|---|---|
| `200` | Consulta e atualização bem-sucedidas |
| `201` | Criação bem-sucedida |
| `204` | Exclusão bem-sucedida |
| `400` | Validação ou regra de entrada inválida |
| `404` | Recurso não encontrado |
| `409` | Conflito de integridade ou unicidade |
| `500` | Erro inesperado no servidor |


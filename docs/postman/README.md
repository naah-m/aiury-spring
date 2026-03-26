# Guia de Validacao com Postman

## 1. Arquivos desta pasta
- `Aiury-Sprint3.postman_collection.json`: collection oficial da Sprint 3.
- `Aiury-local.postman_environment.json`: environment para execucao local.
- `payloads/`: exemplos de payload por recurso.

## 2. Importacao
1. Abra o Postman.
2. Importe `Aiury-Sprint3.postman_collection.json`.
3. Importe `Aiury-local.postman_environment.json`.
4. Selecione o environment `Aiury Local`.

## 3. Variaveis do environment
- `baseUrl`: URL base da API (default: `http://localhost:8080`).
- `estadoId`, `cidadeId`, `usuarioId`, `ajudanteId`, `chatId`, `mensagemId`: preenchidas ao longo do fluxo.

## 4. Ordem recomendada de execucao
1. `Catalogos > Criar estado`
2. `Catalogos > Criar cidade`
3. `Usuarios > Criar usuario`
4. `Ajudantes > Criar ajudante`
5. `Chats > Criar chat`
6. `Mensagens > Criar mensagem`
7. Requests de listagem com filtros
8. Pasta `Erros Controlados`

## 5. Atalho para demo com seed Oracle
Com a aplicacao iniciada e Flyway executado, voce pode iniciar direto por requests de listagem:
- `GET /api/estados`
- `GET /api/cidades`
- `GET /api/usuarios`
- `GET /api/ajudantes`
- `GET /api/chats`
- `GET /api/mensagens`

Depois, use os IDs retornados para updates/deletes.

## 6. Padrao de data para requests
- `dataNascimento`: `dd/MM/yyyy`
- `dataInicio` e `dataFim`: `dd/MM/yyyy HH:mm:ss`
- `dataEnvio`: `dd/MM/yyyy HH:mm:ss`

## 7. Erros mais comuns
- `404`: recurso referenciado inexistente.
- `409`: conflito de unicidade/integridade.
- `400`: payload invalido ou formato de data incorreto.

## 8. Evidencias para avaliacao
- print de `201` para cada recurso criado;
- print de `200` em listagens com filtro;
- print de `400` com `validationErrors`;
- print de `404` para busca de recurso inexistente.

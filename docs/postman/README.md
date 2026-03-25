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
- `baseUrl`: URL base da API. Default: `http://localhost:8080`.
- `estadoId`: preenchido apos `POST /api/estados`.
- `cidadeId`: preenchido apos `POST /api/cidades`.
- `usuarioId`: preenchido apos `POST /api/usuarios`.
- `ajudanteId`: preenchido apos `POST /api/ajudantes`.
- `chatId`: preenchido apos `POST /api/chats`.
- `mensagemId`: preenchido apos `POST /api/mensagens`.

## 4. Ordem recomendada de execucao
1. `Catalogos > Criar estado`
2. `Catalogos > Criar cidade`
3. `Usuarios > Criar usuario`
4. `Ajudantes > Criar ajudante`
5. `Chats > Criar chat`
6. `Mensagens > Criar mensagem`
7. Requests de listagem com filtros
8. Pasta `Erros Controlados`

## 5. Cenarios cobertos na collection
- Fluxo completo de cadastro e vinculacao entre recursos.
- Filtros por `estadoId`, `cidadeId`, `usuarioId`, `chatId`.
- Captura automatica de IDs para as proximas requests.
- Validacao de erro `400` (payload invalido).
- Validacao de erro `404` (recurso inexistente).

## 6. Evidencias que devem ser salvas para avaliacao
- Print de `201` para cada recurso criado.
- Print de `200` em listagens com filtro.
- Print de `400` com `validationErrors`.
- Print de `404` para busca de recurso inexistente.
- Export atualizado da collection apos execucao final.

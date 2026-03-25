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

## 4.1 Atalho para demo com seed local (`dev`)
Quando a aplicacao sobe no profile `dev`, uma seed minima e aplicada automaticamente.
Com isso, voce pode iniciar direto pelas requests de `GET` (listagens) para gravacao de video:
- `GET /api/estados`
- `GET /api/cidades`
- `GET /api/usuarios`
- `GET /api/ajudantes`
- `GET /api/chats`
- `GET /api/mensagens`

Depois, use os IDs retornados para executar updates/deletes sem precisar criar tudo do zero.

## 5. Padrao de data para requests
- `dataNascimento` (`Usuario`): `dd/MM/yyyy`
- `dataInicio` e `dataFim` (`Chat`): `dd/MM/yyyy HH:mm:ss`
- `dataEnvio` (`Mensagem`): `dd/MM/yyyy HH:mm:ss`

Exemplos validos:
- `15/08/1998`
- `25/03/2026 14:00:00`

## 6. Cenarios cobertos na collection
- Fluxo completo de cadastro e vinculacao entre recursos.
- Filtros por `estadoId`, `cidadeId`, `usuarioId`, `chatId`.
- Captura automatica de IDs para as proximas requests.
- Validacao de erro `400` (payload invalido).
- Validacao de erro `404` (recurso inexistente).
- Validacao de erro `400` para formato de data invalido.

## 7. Erros mais comuns ao testar
- `404`: tentar criar `Usuario` sem `cidadeId` existente, ou `Chat` sem `usuarioId/ajudanteId` existentes.
- `409`: cadastrar `Usuario` com celular repetido.
- `400`: enviar data no formato ISO (`2026-03-25T14:00:00`) em vez de `25/03/2026 14:00:00`.

## 8. Evidencias que devem ser salvas para avaliacao
- Print de `201` para cada recurso criado.
- Print de `200` em listagens com filtro.
- Print de `400` com `validationErrors`.
- Print de `404` para busca de recurso inexistente.
- Export atualizado da collection apos execucao final.

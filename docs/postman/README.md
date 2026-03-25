# Guia de Uso - Postman (Aiury API)

## 1. Conteudo da Pasta
- `Aiury-Sprint3.postman_collection.json`: collection oficial da entrega.
- `Aiury-local.postman_environment.json`: environment com variaveis de ambiente local.
- `payloads/`: exemplos de corpo para requisicoes de criacao e atualizacao.

## 2. Como Importar
1. Abrir o Postman.
2. Importar a collection `Aiury-Sprint3.postman_collection.json`.
3. Importar o environment `Aiury-local.postman_environment.json`.
4. Selecionar o environment `Aiury Local`.

## 3. Variaveis do Environment
- `baseUrl`: URL base da API (default: `http://localhost:8080`)
- `usuarioId`: preenchido apos criar usuario
- `ajudanteId`: preenchido apos criar ajudante
- `chatId`: preenchido apos criar chat
- `mensagemId`: preenchido apos criar mensagem

## 4. Ordem Recomendada de Execucao
1. `POST /api/usuarios`
2. `POST /api/ajudantes`
3. `POST /api/chats`
4. `POST /api/mensagens`
5. `GET` de listagens com filtros
6. `PUT` de atualizacao dos recursos
7. `DELETE` de encerramento dos recursos
8. Cenarios de erro (`400`, `404`, `409`)

## 5. Arquivos de Payload de Apoio
- Usuario: `usuario-create.json`, `usuario-update.json`, `usuario-invalid-date.json`
- Ajudante: `ajudante-create.json`, `ajudante-update.json`
- Chat: `chat-create.json`, `chat-update.json`
- Mensagem: `mensagem-create.json`, `mensagem-update.json`

## 6. Evidencias Esperadas para Avaliacao
- Captura de tela dos principais requests com status de sucesso.
- Captura de tela de erros controlados com payload `ApiErrorResponse`.
- Export atualizado da collection apos rodada completa de testes.

# Guia Postman - Aiury API

## Arquivos da pasta
- `Aiury-Sprint3.postman_collection.json`: colecao com endpoints principais.
- `Aiury-local.postman_environment.json`: environment local com variavel `baseUrl`.
- `payloads/`: exemplos de corpos JSON para requests.

## Como importar
1. Abrir Postman.
2. Importar `Aiury-Sprint3.postman_collection.json`.
3. Importar `Aiury-local.postman_environment.json`.
4. Selecionar o environment `Aiury Local`.

## Variaveis de ambiente
- `baseUrl`: `http://localhost:8080`
- `usuarioId`: ID dinamico apos criar usuario
- `ajudanteId`: ID dinamico apos criar ajudante
- `chatId`: ID dinamico apos criar chat
- `mensagemId`: ID dinamico apos criar mensagem

## Fluxo recomendado de teste manual
1. Criar usuario
2. Criar ajudante
3. Criar chat
4. Criar mensagem
5. Executar `GET all` com filtros
6. Atualizar recursos
7. Deletar recursos

## Evidencias esperadas
- Capturas de tela dos requests com status `201`, `200`, `204`, `400` e `404`.
- Export da collection apos executar o fluxo completo.

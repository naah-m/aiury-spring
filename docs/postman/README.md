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

## 4. Autenticacao obrigatoria para API
A API e protegida por sessao autenticada.

Fluxo recomendado no Postman:
1. executar `POST {{baseUrl}}/login` como `x-www-form-urlencoded`;
2. enviar `username` e `password` de um perfil valido;
3. manter cookies habilitados para reuso do `JSESSIONID` nas requests seguintes.

Sem autenticacao:
- endpoints protegidos retornam `401` ou `403`.

## 5. Ordem recomendada de execucao
1. `Catalogos > Criar estado`
2. `Catalogos > Criar cidade`
3. `Usuarios > Criar usuario`
4. `Ajudantes > Criar ajudante`
5. `Chats > Criar chat`
6. `Mensagens > Criar mensagem`
7. Requests de listagem com filtros
8. Pasta `Erros Controlados`

## 6. Atalho para demo com seed Oracle
Com aplicacao iniciada e Flyway concluido:
1. autentique com credenciais validas;
2. execute listagens para coletar IDs:
   - `GET /api/estados`
   - `GET /api/cidades`
   - `GET /api/usuarios`
   - `GET /api/ajudantes`
   - `GET /api/chats`
   - `GET /api/mensagens`

## 7. Padrao de data para requests
- `dataNascimento`: `dd/MM/yyyy`
- `dataInicio` e `dataFim`: `dd/MM/yyyy HH:mm:ss`
- `dataEnvio`: `dd/MM/yyyy HH:mm:ss`

## 8. Erros mais comuns
- `400`: payload invalido, regra de negocio ou formato de data incorreto.
- `401`: sem autenticacao.
- `403`: perfil sem permissao.
- `404`: recurso referenciado inexistente.
- `409`: conflito de unicidade/integridade.

## 9. Evidencias recomendadas para avaliacao
- resposta `201` para criacao dos recursos principais;
- resposta `200` para consultas por perfil;
- resposta `401` para chamada sem autenticacao;
- resposta `403` para perfil sem permissao;
- resposta `400` com `validationErrors`;
- resposta `404` e `409` em cenarios de erro controlado.

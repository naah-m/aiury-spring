# Estrategia de Testes e Evidencias

## Objetivo
Garantir confiabilidade minima da entrega por meio de testes automatizados e roteiro de testes manuais.

## Testes Automatizados Implementados

| Tipo | Arquivo | Cobertura principal |
|---|---|---|
| Contexto Spring | `AiuryApplicationTests` | Sobe contexto da aplicacao com perfil de teste |
| Repository | `AjudanteRepositoryTest` | Valida query method `findByDisponivel` |
| Service (unitario) | `UsuarioServiceImplTest` | Valida regra de criacao de usuario e erro de cidade inexistente |
| Controller (WebMvc) | `UsuarioControllerTest` | Valida retorno `GET /api/usuarios/{id}` com HATEOAS |

## Comandos Oficiais de Validacao

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd clean package
```

## Ambiente de Teste
- Perfil ativo: `test`
- Banco: H2 em memoria (`src/test/resources/application-test.properties`)
- Estrategia de schema: `create-drop`

## Testes Manuais de API (Postman)
Arquivos:
- Colecao: `docs/postman/Aiury-Sprint3.postman_collection.json`
- Environment: `docs/postman/Aiury-local.postman_environment.json`
- Payloads: `docs/postman/payloads/`

## Evidencias Esperadas para Entrega
- Print do resultado de `mvn clean test` sem falhas.
- Print do resultado de `mvn clean package` sem falhas.
- Print da execucao dos principais endpoints no Postman.
- Print do Swagger com os quatro recursos documentados.

## Criticos a Validar no Dia da Apresentacao
- Criacao de usuario e ajudante.
- Criacao de chat com referencias validas.
- Criacao de mensagem vinculada ao chat.
- Filtros de listagem (`cidadeId`, `disponivel`, `status`, `chatId`, `remetenteId`).
- Erros 400 e 404 com payload padronizado.

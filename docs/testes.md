# Estrategia de Testes e Evidencias

## 1. Objetivo
Comprovar que a entrega Sprint 3 esta:
- compilando sem quebra;
- com repositories consistentes;
- com regras de negocio validadas;
- com API documentada e testavel.

## 2. Validacao automatizada oficial

Comandos obrigatorios:

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd clean package
```

Resultado esperado:
- `BUILD SUCCESS` nos dois comandos.

## 3. Cobertura automatizada atual

| Camada | Classe de teste | Cobertura principal |
|---|---|---|
| Contexto | `AiuryApplicationTests` | Inicializacao da aplicacao com perfil `test` |
| Controller | `UsuarioControllerTest` | HATEOAS no GET por ID e erro de validacao 400 |
| Service | `UsuarioServiceImplTest` | Validacao de cidade inexistente e criacao de usuario |
| Service | `EstadoServiceImplTest` | Regras de conflito para nome/UF duplicados |
| Repository | `AjudanteRepositoryTest` | Filtro por disponibilidade |
| Repository | `CidadeRepositoryTest` | Filtro por estado com ordenacao |
| Repository | `EstadoRepositoryTest` | Busca por UF ignorando case |
| Repository | `UsuarioRepositoryTest` | Filtro por cidade |
| Repository | `MensagemRepositoryTest` | Filtros de mensagem por chat/remetente com ordenacao |

## 4. Ambiente de teste automatizado
- Perfil: `test`
- Banco: H2 em memoria
- Config: `src/test/resources/application-test.properties`
- DDL: `create-drop`

## 5. Validacao manual via Postman

Arquivos oficiais:
- Collection: `docs/postman/Aiury-Sprint3.postman_collection.json`
- Environment: `docs/postman/Aiury-local.postman_environment.json`
- Guia: `docs/postman/README.md`

Ordem recomendada:
1. Catalogos (`estado`, `cidade`)
2. Usuario
3. Ajudante
4. Chat
5. Mensagem
6. Filtros de listagem
7. Cenarios de erro

## 6. Matriz de cenarios minimos por endpoint

| Cenario | Endpoint | Metodo | Esperado |
|---|---|---|---|
| Criar estado valido | `/api/estados` | POST | `201` + `_links` |
| Criar cidade valida | `/api/cidades` | POST | `201` + link para estado |
| Criar usuario valido | `/api/usuarios` | POST | `201` + link para cidade/estado |
| Criar ajudante valido | `/api/ajudantes` | POST | `201` |
| Criar chat valido | `/api/chats` | POST | `201` + links usuario/ajudante/mensagens |
| Criar mensagem valida | `/api/mensagens` | POST | `201` |
| Filtro de usuarios por cidade | `/api/usuarios?cidadeId={id}` | GET | `200` |
| Filtro de chats por usuario | `/api/chats?usuarioId={id}` | GET | `200` |
| Filtro de mensagens por chat | `/api/mensagens?chatId={id}` | GET | `200` |
| Payload invalido de usuario | `/api/usuarios` | POST | `400` com `validationErrors` |
| Recurso inexistente | `/api/usuarios/999999` | GET | `404` |

## 7. Evidencias para anexar na entrega
- Log de terminal com `BUILD SUCCESS` em `clean test`.
- Log de terminal com `BUILD SUCCESS` em `clean package`.
- Captura do Swagger com os 6 recursos (`Estados`, `Cidades`, `Usuarios`, `Ajudantes`, `Chats`, `Mensagens`).
- Capturas de requests no Postman para `201`, `200`, `204`, `400`, `404`, `409`.
- Export final da collection apos execucao.

## 8. Gap conhecido para evolucao futura
- Incluir testes de controller para `Cidade`, `Estado`, `Chat` e `Mensagem`.
- Adicionar testes de integracao de regra de negocio em `MensagemServiceImpl`.

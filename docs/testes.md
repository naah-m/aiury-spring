# Estrategia de Testes e Evidencias de Validacao

## 1. Objetivo
Comprovar estabilidade tecnica da entrega por meio de:
- testes automatizados em camadas-chave;
- roteiro manual reproduzivel via Postman;
- evidencias de build e execucao.

## 2. Testes Automatizados Implementados
| Camada | Arquivo | Cobertura |
|---|---|---|
| Contexto | `AiuryApplicationTests` | Inicializacao do contexto Spring com perfil `test` |
| Controller | `UsuarioControllerTest` | `GET /api/usuarios/{id}` com HATEOAS e validacao `400` para payload invalido |
| Service | `UsuarioServiceImplTest` | Regra de criacao e validacao de cidade inexistente |
| Repository | `AjudanteRepositoryTest` | Query derivada por disponibilidade |
| Repository | `UsuarioRepositoryTest` | Query derivada por cidade |
| Repository | `MensagemRepositoryTest` | Queries derivadas de filtro e ordenacao de mensagens |

## 3. Comandos Oficiais de Validacao
```powershell
.\mvnw.cmd clean test
.\mvnw.cmd clean package
```

## 4. Ambiente de Testes
- Perfil: `test`
- Banco: H2 em memoria
- Configuracao: `src/test/resources/application-test.properties`
- Estrategia de schema: `create-drop`

## 5. Validacao Manual da API (Postman)
Arquivos:
- Collection: `docs/postman/Aiury-Sprint3.postman_collection.json`
- Environment: `docs/postman/Aiury-local.postman_environment.json`
- Payloads: `docs/postman/payloads/`

## 6. Fluxo Recomendado de Teste Manual
1. Criar usuario.
2. Criar ajudante.
3. Criar chat com `usuarioId` e `ajudanteId`.
4. Criar mensagem com `chatId` e `remetenteId`.
5. Validar filtros de listagem.
6. Validar atualizacao de recursos.
7. Validar exclusao de recursos.
8. Validar cenarios de erro (`400`, `404`, `409`).

## 7. Matriz de Cenarios
| Caso de teste | Endpoint | Metodo | Payload | Resultado esperado |
|---|---|---|---|---|
| Criar usuario valido | `/api/usuarios` | POST | `payloads/usuario-create.json` | `201 Created` com links HATEOAS |
| Criar usuario com data invalida | `/api/usuarios` | POST | `payloads/usuario-invalid-date.json` | `400 Bad Request` com `validationErrors` |
| Listar usuarios por cidade | `/api/usuarios?cidadeId=1` | GET | N/A | `200 OK` filtrado |
| Criar ajudante valido | `/api/ajudantes` | POST | `payloads/ajudante-create.json` | `201 Created` |
| Filtrar ajudantes disponiveis | `/api/ajudantes?disponivel=true` | GET | N/A | `200 OK` |
| Criar chat valido | `/api/chats` | POST | `payloads/chat-create.json` | `201 Created` com links para usuario/ajudante/mensagens |
| Filtrar chat por status | `/api/chats?status=EM_ANDAMENTO` | GET | N/A | `200 OK` |
| Criar mensagem valida | `/api/mensagens` | POST | `payloads/mensagem-create.json` | `201 Created` |
| Filtrar mensagens por chat e remetente | `/api/mensagens?chatId={id}&remetenteId={id}` | GET | N/A | `200 OK` ordenado por data |
| Buscar recurso inexistente | `/api/usuarios/99999` | GET | N/A | `404 Not Found` com `ApiErrorResponse` |

## 8. Evidencias para Entrega
- Print do terminal com `BUILD SUCCESS` em `clean test`.
- Print do terminal com `BUILD SUCCESS` em `clean package`.
- Prints de requests no Postman para `201`, `200`, `204`, `400`, `404`.
- Print do Swagger UI com os quatro recursos documentados.

## 9. Criterios de Aprovacao Interna
- Nenhum teste automatizado falhando.
- Nenhuma query derivada quebrada por nome de campo.
- Respostas de erro padronizadas.
- Endpoints documentados e consistentes com implementacao real.

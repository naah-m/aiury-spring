# Aiury API

Backend Spring Boot para a plataforma Aiury, focada em acolhimento emocional com trilha estruturada de atendimento.

## 1. Contexto do problema
Atendimentos informais sem registro dificultam continuidade e analise de qualidade. A API organiza esse fluxo em recursos REST relacionais, com validacao, rastreabilidade e documentacao.

## 2. Objetivo da solucao
- Gerenciar catalogos geograficos (`Estado` e `Cidade`).
- Cadastrar usuarios e ajudantes.
- Abrir, acompanhar e encerrar chats.
- Registrar mensagens vinculadas ao chat.
- Entregar API REST nivel 3 com HATEOAS, tratamento padrao de erros e OpenAPI.
- Padronizar relacoes HATEOAS (`criar`, `atualizar`, `excluir`, relacionamentos de dominio).
- Padronizar datas em toda a API (`dd/MM/yyyy` e `dd/MM/yyyy HH:mm:ss`).

## 3. Publico-alvo
- Equipe academica avaliadora.
- Equipe tecnica do projeto (backend/frontend).
- Time que vai evoluir a plataforma nas proximas sprints.

## 4. Arquitetura
Arquitetura em camadas:
- `controller`: contrato HTTP e status codes.
- `services`: regras de negocio e validacao de dominio.
- `repositories`: persistencia Spring Data JPA.
- `entities`: modelo relacional JPA.
- `dto`: contratos request/response.
- `mappers`: conversao DTO <-> entidade.
- `representation`: composicao HATEOAS.
- `configs` e `exceptions`: OpenAPI e erros globais.

Detalhes: `docs/arquitetura.md`.

## 5. Tecnologias
- Java 21
- Spring Boot 3.5.6
- Spring Web
- Spring Data JPA
- Spring Validation
- Spring HATEOAS
- Springdoc OpenAPI (Swagger UI)
- Maven Wrapper
- Oracle JDBC (perfil `oracle`)
- H2 (perfil `dev` e testes)
- JUnit 5 e Mockito

## 6. Estrutura do projeto

```text
src/main/java/br/com/fiap/aiury
- controller
- services
- repositories
- entities
- dto
- mappers
- representation
- configs
- exceptions

src/test/java/br/com/fiap/aiury
- controller
- services
- repositories

docs
- arquitetura.md
- cronograma.md
- modelagem.md
- endpoints.md
- testes.md
- postman/
```

## 7. Como executar

### 7.1 Perfil padrao (`dev`)
O projeto sobe com H2 por default:

```powershell
.\mvnw.cmd spring-boot:run
```

### 7.2 Seed local para demo (automatico no `dev`)
Ao subir no profile `dev`, a aplicacao carrega uma seed minima controlada para facilitar testes e apresentacao:
- 2 estados (`SP`, `RJ`)
- 3 cidades (`Sao Paulo`, `Campinas`, `Rio de Janeiro`)
- 2 usuarios de dominio (`Camila Nunes`, `Diego Pereira`)
- 2 ajudantes
- 2 chats e 3 mensagens

Credenciais do painel web (Spring Security):
- `admin / admin123` (perfil `ADMIN`)
- `atendente / atendente123` (perfil `ATENDENTE`)

Senha padrao dos usuarios de dominio seed:
- `demo12345`

Para desabilitar a seed local:

```powershell
$env:AIURY_SEED_ENABLED="false"
.\mvnw.cmd spring-boot:run
```

### 7.3 Perfil Oracle (`oracle`)

```powershell
$env:SPRING_PROFILES_ACTIVE="oracle"
$env:DB_URL="jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl"
$env:DB_USERNAME="SEU_USUARIO"
$env:DB_PASSWORD="SUA_SENHA"
.\mvnw.cmd spring-boot:run
```

## 8. Configuracao de banco
- Arquivo base: `src/main/resources/application.properties`
- Perfil local: `src/main/resources/application-dev.properties`
- Perfil Oracle: `src/main/resources/application-oracle.properties`
- Perfil de teste: `src/test/resources/application-test.properties`

## 9. Swagger / OpenAPI
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- Alias: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

### 9.1 Padrao oficial de data/hora na API
- Campos de data sem hora (`LocalDate`): `dd/MM/yyyy` (ex.: `15/08/1998`).
- Campos de data/hora (`LocalDateTime`): `dd/MM/yyyy HH:mm:ss` (ex.: `25/03/2026 14:00:00`).
- O mesmo formato vale para request e response.
- Payload com data fora desse padrao retorna `400 Bad Request` com mensagem de formato invalido.

## 10. Endpoints da API

| Recurso | Base path | Operacoes |
|---|---|---|
| Root | `/api` | `GET` |
| Estados | `/api/estados` | `GET`, `GET/{id}`, `POST`, `PUT/{id}`, `DELETE/{id}` |
| Cidades | `/api/cidades` | `GET`, `GET/{id}`, `POST`, `PUT/{id}`, `DELETE/{id}` |
| Usuarios | `/api/usuarios` | `GET`, `GET/{id}`, `POST`, `PUT/{id}`, `DELETE/{id}` |
| Ajudantes | `/api/ajudantes` | `GET`, `GET/{id}`, `POST`, `PUT/{id}`, `DELETE/{id}` |
| Chats | `/api/chats` | `GET`, `GET/{id}`, `POST`, `PUT/{id}`, `DELETE/{id}` |
| Mensagens | `/api/mensagens` | `GET`, `GET/{id}`, `POST`, `PUT/{id}`, `DELETE/{id}` |

Detalhes completos, filtros e exemplos: `docs/endpoints.md`.

## 11. Modelagem do dominio
- Entidades: `Estado`, `Cidade`, `Usuario`, `Ajudante`, `Chat`, `Mensagem`.
- `Usuario` referencia `Cidade`; `Estado` e derivado pela cidade.
- `Cidade` usa unicidade composta por estado.
- Regras adicionais de consistencia em `Chat` e `Mensagem`.

Detalhes: `docs/modelagem.md`.

## 12. DER e diagrama de classes
- DER final: `docs/imagens/der.png`
- Diagrama de classes final: `docs/imagens/diagrama-classes.png`

## 13. Testes

### 13.1 Automatizados

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd clean package
```

### 13.2 Manuais
- Collection: `docs/postman/Aiury-Sprint3.postman_collection.json`
- Environment: `docs/postman/Aiury-local.postman_environment.json`
- Guia: `docs/postman/README.md`

Fluxo recomendado para evitar erros de ID inexistente:
1. Criar `Estado`
2. Criar `Cidade` usando `estadoId`
3. Criar `Usuario` usando `cidadeId`
4. Criar `Ajudante`
5. Criar `Chat` usando `usuarioId` e `ajudanteId`
6. Criar `Mensagem` usando `chatId` e `remetenteId` (usuario do chat)

Documento de evidencias: `docs/testes.md`.

## 14. Integrantes

| Nome | Identificacao academica | Papel principal |
|---|---|---|
| Rafael (owner do repositorio) | inserir RM oficial | Backend, modelagem, revisao tecnica |
| Demais integrantes do grupo | inserir RM oficial | Documentacao final, evidencias e apresentacao |

## 15. Responsabilidade por integrante
- Backend e persistencia: implementacao e correcao de entidades, repositories, services e controllers.
- Documentacao tecnica: README, endpoints, modelagem, cronograma.
- Evidencias e apresentacao: Postman, capturas de validacao, video final.

## 16. Link do video
- Inserir link final de apresentacao da Sprint 3 apos gravacao.

## 17. Conclusao
O projeto foi consolidado para uma entrega academica mais madura: build estavel, modelagem consistente, catalogos geograficos integrados, HATEOAS padronizado, regras de negocio explicitas, Swagger atualizado e trilha de testes documentada.

# Aiury - Sprint 3 (Java Advanced)

Aplicacao Spring Boot com frontend server-side em Thymeleaf para gestao de atendimentos entre usuario e ajudante, com persistencia relacional, migracoes versionadas e controle de acesso por perfil.

## 1. Visão geral
O projeto entrega uma base completa de Sprint 3 com:
- frontend web funcional (`/`, `/login`, `/app`, `/app/chats`, `/app/usuarios`, `/app/ajudantes`);
- API REST com HATEOAS (`/api/**`);
- seguranca com Spring Security (perfis `ADMIN`, `USUARIO` e `AJUDANTE`);
- banco versionado com Flyway para H2 e Oracle;
- seed controlada para demonstracao local.

## 2. Problema resolvido
O dominio do projeto exige registrar atendimentos de forma estruturada. Antes, o fluxo era informal e sem rastreabilidade. A solucao implementada organiza:
- catalogo geografico (`Estado` e `Cidade`);
- cadastro de usuarios e ajudantes;
- abertura e acompanhamento de chat;
- histórico de mensagens por atendimento;
- controle de quem pode consultar ou alterar dados.

## 3. Arquitetura resumida
Arquitetura em camadas, com separação clara de responsabilidades:
- `controller` (REST e MVC): entrada HTTP, views e respostas;
- `services`: regras de negocio e validações de dominio;
- `repositories`: persistencia com Spring Data JPA;
- `entities`: modelo relacional;
- `dto` e `mappers`: contratos de entrada/saida e transformacoes;
- `representation`: montagem HATEOAS da API;
- `configs` e `security`: configuracoes globais, Flyway, excecoes e autenticação.

No frontend MVC, os fluxos de chat estao separados para manter codigo limpo:
- `ChatMvcController`: listagem, abertura e status;
- `ChatConversationMvcController`: conversa e envio de mensagens;
- `ChatMvcViewSupport`: composicao de dados de tela e status.

## 4. Tecnologias utilizadas
- Java 21
- Spring Boot 3.5.6
- Spring Web + Thymeleaf
- Spring Security
- Spring Data JPA
- Bean Validation
- Spring HATEOAS
- Flyway
- H2 (dev/test)
- Oracle (profile `oracle`)
- Springdoc OpenAPI (Swagger UI)
- JUnit 5 / Spring Test

## 5. Como configurar o banco
Arquivos de configuração:
- `src/main/resources/application.properties` (base comum)
- `src/main/resources/application-dev.properties` (H2)
- `src/main/resources/application-oracle.properties` (Oracle)
- `src/main/resources/messages.properties` (mensagens padrão)
- `src/main/resources/messages_pt_BR.properties` (mensagens em português-BR)
- `src/test/resources/application-test.properties` (testes)

### 5.1 Ambiente local (padrao `dev`)
Por default, a aplicacao sobe com H2 em memoria:
- URL: `jdbc:h2:mem:aiury_dev;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`
- Usuario: `sa`
- Senha: vazia

Opcionalmente, voce pode sobrescrever com variaveis:
- `DEV_DB_URL`
- `DEV_DB_USERNAME`
- `DEV_DB_PASSWORD`

### 5.2 Ambiente Oracle (`oracle`)
Defina:
- `SPRING_PROFILES_ACTIVE=oracle`

### 5.3 Idioma e codificação
- Locale fixo da aplicação: `pt_BR`
- Mensagens Spring (`MessageSource`) em UTF-8
- Thymeleaf e servlet configurados para UTF-8
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

Exemplo (PowerShell):

```powershell
$env:SPRING_PROFILES_ACTIVE="oracle"
$env:DB_URL="jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl"
$env:DB_USERNAME="SEU_USUARIO"
$env:DB_PASSWORD="SUA_SENHA"
.\mvnw.cmd spring-boot:run
```

## 6. Como rodar a aplicacao
Pre-requisitos:
- Java 21+
- Maven Wrapper (já incluso no repositorio)

### 6.1 Execucao rapida (Windows)

```powershell
.\mvnw.cmd clean spring-boot:run
```

### 6.2 Execucao rapida (Linux/macOS)

```bash
./mvnw clean spring-boot:run
```

Aplicacao local:
- `http://localhost:8080`

### 6.3 Seed inicial para demonstracao
No profile `dev`, a seed local sobe automaticamente (`AIURY_SEED_ENABLED=true`) com dados minimos para demo:
- 2 estados (`SP`, `RJ`)
- 3 cidades (`Sao Paulo`, `Campinas`, `Rio de Janeiro`)
- 2 usuarios (`Camila Nunes`, `Diego Pereira`)
- 2 ajudantes
- 2 chats
- 5 mensagens

Desabilitar seed:

```powershell
$env:AIURY_SEED_ENABLED="false"
.\mvnw.cmd spring-boot:run
```

## 7. Como o Flyway funciona no projeto
O Flyway esta habilitado por padrao e executa no startup:
- `spring.flyway.enabled=true`
- `spring.flyway.locations=classpath:db/migration/{vendor}`
- `spring.flyway.baseline-on-migrate=true`
- `spring.flyway.validate-on-migrate=true`

Scripts versionados:
- H2: `src/main/resources/db/migration/h2/V1__create_core_tables.sql`, `V2__create_indexes.sql`, `V3__security_roles_and_message_sender.sql`
- Oracle: `src/main/resources/db/migration/oracle/V1__create_core_tables.sql`, `V2__create_indexes.sql`, `V3__security_roles_and_message_sender.sql`

Em resumo: ao iniciar a aplicacao, o Flyway detecta o vendor do datasource, valida o histórico e aplica somente migracoes pendentes.

## 8. Como acessar o frontend
URLs principais:
- Home: `http://localhost:8080/`
- Login: `http://localhost:8080/login`
- Dashboard (apos login): `http://localhost:8080/app`
- Chats: `http://localhost:8080/app/chats`
- Usuarios: `http://localhost:8080/app/usuarios`
- H2 Console (dev): `http://localhost:8080/h2-console`

## 9. Como acessar a API
Base local:
- `http://localhost:8080/api`

Documentacao:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

Regra de acesso:
- API sempre autenticada;
- `POST/PUT/DELETE /api/usuarios` e `/api/ajudantes`: `ADMIN`;
- `GET/POST /api/mensagens`: `ADMIN`, `USUARIO`, `AJUDANTE` com validação de vínculo;
- `GET /api/chats`: `ADMIN`, `USUARIO`, `AJUDANTE` com validação de vínculo.

## 10. Como fazer login
Use a tela `/login`:
- `admin` / `admin123`
- `11999998888` / `demo12345` (usuario seed)
- `ajudante.escuta` / `apoio12345` (ajudante seed)

Depois de autenticar, o redirecionamento padrao e para `/app`.

## 11. Perfis de usuario
Perfis da camada de seguranca web:

| Perfil | Credencial | Acesso principal |
|---|---|---|
| `ADMIN` | `admin / admin123` | Acesso total ao painel web e API |
| `USUARIO` | `celular / senha` | Abre o proprio chat e acessa apenas chats/mensagens vinculados |
| `AJUDANTE` | `login / senha` | Acessa apenas chats/mensagens sob sua responsabilidade |

Regras relevantes:
- `USUARIO` pode abrir novo chat web em `/app/chats/novo` com vinculacao automatica;
- `USUARIO` so pode ter 1 chat ativo por vez (`INICIADO` ou `EM_ANDAMENTO`);
- `AJUDANTE` não abre chat novo no frontend;
- `ADMIN` gerencia usuarios/ajudantes e pode criar/editar chats.

## 12. Fluxos principais implementados
Fluxos Sprint 3 entregues ponta a ponta com Thymeleaf, validação e persistencia real:

1. Abertura e acompanhamento de atendimento/chat
- usuario abre o proprio chat (`/app/chats/novo`) com atribuicao automatica de ajudante;
- regra de bloqueio para impedir mais de 1 chat ativo por usuario;
- admin pode abrir chat manual com selecao de usuario/ajudante;
- listar e filtrar chats (`/app/chats`);
- ver detalhes e atualizar status (`/app/chats/{id}`);
- feedback visual de sucesso/erro e validação de formulario.

2. Troca de mensagens no atendimento
- abrir tela de conversa (`/app/chats/{id}/conversa`);
- enviar mensagem com validação;
- carregar histórico de mensagens em ordem;
- bloqueio de envio quando chat esta finalizado.

Complemento de apoio para demo:
- gestao de usuarios (`/app/usuarios`, `/app/usuarios/novo`).

## 13. Instrucoes de teste
### 13.1 Testes automatizados

```powershell
.\mvnw.cmd clean test
```

Opcional (build completo):

```powershell
.\mvnw.cmd clean package
```

### 13.2 Teste manual web (roteiro rapido para banca)
1. Subir a aplicacao em `dev`.
2. Acessar `/login` com `11999998888/demo12345`.
3. Ir em `/app/chats/novo` e abrir um chat.
4. Repetir abertura para validar bloqueio de chat ativo.
5. Abrir conversa e enviar mensagens.
6. Logar com `admin/admin123` e validar gestao administrativa.

### 13.3 Teste manual da API
- Collection: `docs/postman/Aiury-Sprint3.postman_collection.json`
- Environment: `docs/postman/Aiury-local.postman_environment.json`
- Guia: `docs/postman/README.md`

## 14. Estrutura do projeto

```text
src/main/java/br/com/fiap/aiury
|- controller
|  |- ApiRootController, EstadoController, CidadeController, UsuarioController, ...
|  |- mvc (HomeMvcController, LoginMvcController, ChatMvcController, ChatConversationMvcController, UsuarioMvcController)
|- services
|- repositories
|- entities
|- dto
|- mappers
|- representation
|- security
|- configs
|- exceptions

src/main/resources
|- templates (layout, fragments, app/chats, app/usuarios, auth, error, home)
|- static/css
|- db/migration/h2
|- db/migration/oracle
|- application*.properties

src/test/java/br/com/fiap/aiury
|- controller
|- repositories
|- services
|- security

docs
|- arquitetura.md
|- endpoints.md
|- modelagem.md
|- testes.md
|- postman/
```

## 15. Integrantes
Conforme histórico de contribuicoes do repositorio:

| Nome | Contato |
|---|---|
| Rafael (rafael051) | rafael.cooperativas@gmail.com |
| Nathalia Mantovani | nathaliamantovani22@gmail.com |
| Renato1910 | renato.angelo.1910@gmail.com |

## 16. Link do video
Video de demonstracao da Sprint 3:
- publicacao pendente no repositorio nesta revisao de 25/03/2026.


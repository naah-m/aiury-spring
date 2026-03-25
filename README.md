# Aiury - Sprint 3 (Java Advanced)

Aplicacao Spring Boot com frontend server-side em Thymeleaf para gestao de atendimentos entre usuario e ajudante, com persistencia relacional, migracoes versionadas e controle de acesso por perfil.

## 1. Visao geral
O projeto entrega uma base completa de Sprint 3 com:
- frontend web funcional (`/`, `/login`, `/app`, `/app/chats`, `/app/usuarios`);
- API REST com HATEOAS (`/api/**`);
- seguranca com Spring Security (perfis `ADMIN` e `ATENDENTE`);
- banco versionado com Flyway para H2 e Oracle;
- seed controlada para demonstracao local.

## 2. Problema resolvido
O dominio do projeto exige registrar atendimentos de forma estruturada. Antes, o fluxo era informal e sem rastreabilidade. A solucao implementada organiza:
- catalogo geografico (`Estado` e `Cidade`);
- cadastro de usuarios e ajudantes;
- abertura e acompanhamento de chat;
- historico de mensagens por atendimento;
- controle de quem pode consultar ou alterar dados.

## 3. Arquitetura resumida
Arquitetura em camadas, com separacao clara de responsabilidades:
- `controller` (REST e MVC): entrada HTTP, views e respostas;
- `services`: regras de negocio e validacoes de dominio;
- `repositories`: persistencia com Spring Data JPA;
- `entities`: modelo relacional;
- `dto` e `mappers`: contratos de entrada/saida e transformacoes;
- `representation`: montagem HATEOAS da API;
- `configs` e `security`: configuracoes globais, Flyway, excecoes e autenticacao.

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
Arquivos de configuracao:
- `src/main/resources/application.properties` (base comum)
- `src/main/resources/application-dev.properties` (H2)
- `src/main/resources/application-oracle.properties` (Oracle)
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
- Maven Wrapper (ja incluso no repositorio)

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
- 3 mensagens

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
- H2: `src/main/resources/db/migration/h2/V1__create_core_tables.sql`, `V2__create_indexes.sql`
- Oracle: `src/main/resources/db/migration/oracle/V1__create_core_tables.sql`, `V2__create_indexes.sql`

Em resumo: ao iniciar a aplicacao, o Flyway detecta o vendor do datasource, valida o historico e aplica somente migracoes pendentes.

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
- `GET /api/**`: liberado sem autenticacao;
- `POST/PUT/DELETE /api/**`: requer perfil `ADMIN`.

## 10. Como fazer login
Use a tela `/login` com usuarios tecnicos definidos em `SecurityConfig`:
- `admin` / `admin123`
- `atendente` / `atendente123`

Depois de autenticar, o redirecionamento padrao e para `/app`.

## 11. Perfis de usuario
Perfis da camada de seguranca web:

| Perfil | Credencial | Acesso principal |
|---|---|---|
| `ADMIN` | `admin / admin123` | Acesso total ao painel web e escrita na API |
| `ATENDENTE` | `atendente / atendente123` | Dashboard, chats e leitura de usuarios |

Regras relevantes:
- `ATENDENTE` pode listar usuarios, mas nao criar/alterar;
- `ADMIN` e `ATENDENTE` podem operar o fluxo de chats no frontend;
- operacoes de escrita da API REST sao restritas a `ADMIN`.

## 12. Fluxos principais implementados
Fluxos Sprint 3 entregues ponta a ponta com Thymeleaf, validacao e persistencia real:

1. Abertura e acompanhamento de atendimento/chat
- abrir novo chat (`/app/chats/novo`);
- listar e filtrar chats (`/app/chats`);
- ver detalhes e atualizar status (`/app/chats/{id}`);
- feedback visual de sucesso/erro e validacao de formulario.

2. Troca de mensagens no atendimento
- abrir tela de conversa (`/app/chats/{id}/conversa`);
- enviar mensagem com validacao;
- carregar historico de mensagens em ordem;
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
2. Acessar `/login` com `admin/admin123`.
3. Ir em `/app/chats` e abrir um novo chat.
4. Entrar em detalhes do chat e atualizar status.
5. Abrir conversa e enviar mensagens.
6. Validar mensagens de erro/sucesso e comportamento com chat finalizado.

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
Conforme historico de contribuicoes do repositorio:

| Nome | Contato |
|---|---|
| Rafael (rafael051) | rafael.cooperativas@gmail.com |
| Nathalia Mantovani | nathaliamantovani22@gmail.com |
| Renato1910 | renato.angelo.1910@gmail.com |

## 16. Link do video
Video de demonstracao da Sprint 3:
- publicacao pendente no repositorio nesta revisao de 25/03/2026.

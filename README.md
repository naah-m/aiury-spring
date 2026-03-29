# Aiury - Sistema de Gestão de Atendimentos

Aplicação web e API REST para gestão de atendimentos entre **USUÁRIOS** e **AJUDANTES**, com autenticação por perfil, regras de vínculo no backend e persistência Oracle com Flyway.

## Sumário
1. [Visão geral](#visão-geral)
2. [Objetivos do projeto](#objetivos-do-projeto)
3. [Funcionalidades principais](#funcionalidades-principais)
4. [Perfis de acesso](#perfis-de-acesso)
5. [Acesso ao sistema em ambiente local](#acesso-ao-sistema-em-ambiente-local)
6. [Como executar o projeto](#como-executar-o-projeto)
7. [Fluxo rápido de avaliação](#fluxo-rápido-de-avaliação)
8. [Como testar o sistema](#como-testar-o-sistema)
9. [Configuração do ambiente](#configuração-do-ambiente)
10. [Estrutura do projeto](#estrutura-do-projeto)
11. [Fluxo de autenticação e autorização](#fluxo-de-autenticação-e-autorização)
12. [Banco de dados e Flyway](#banco-de-dados-e-flyway)
13. [Documentação da API](#documentação-da-api)
14. [Boas práticas e arquitetura](#boas-práticas-e-arquitetura)
15. [Melhorias futuras](#melhorias-futuras)
16. [Documentação complementar](#documentação-complementar)
17. [Integrantes](#integrantes)

## Visão geral
O Aiury organiza o ciclo completo de atendimento em um fluxo auditável:
- cadastro e gestão de perfis de atendimento;
- abertura e acompanhamento de chats;
- troca de mensagens com rastreabilidade;
- controle de acesso por perfil e por vínculo real de atendimento.

Problema atacado:
- evitar atendimento sem histórico confiável;
- reduzir acesso indevido a dados de terceiros;
- entregar operação web e API com regras coerentes.

## Objetivos do projeto
### Objetivo funcional
Disponibilizar um sistema de atendimento com painéis por perfil e regras de negócio consistentes para chats e mensagens.

### Objetivo técnico
Consolidar uma base de Sprint 3 com:
- Spring Boot 3.5.6;
- Spring Security (autenticação/autorização);
- Spring Data JPA + Hibernate;
- Thymeleaf (camada MVC);
- Flyway (migração e seed Oracle);
- OpenAPI/Swagger;
- testes automatizados.

## Funcionalidades principais
### Interface web (MVC)
- login/logout;
- painel inicial com atalhos por perfil;
- gestão de usuários (ADMIN);
- gestão de ajudantes (ADMIN);
- gestão de chats com filtros e detalhes;
- abertura de novo chat para USUÁRIO;
- conversa do chat com envio de mensagens;
- área “Minha conta” com alteração de senha.

### API REST
- recursos HATEOAS em `/api`:
  - `estados`, `cidades`, `usuarios`, `ajudantes`, `chats`, `mensagens`;
- contratos com DTOs de request/response;
- tratamento padronizado de erro.

### Regras de negócio críticas
- usuário não pode ter mais de um chat ativo;
- abertura de chat do usuário seleciona ajudante disponível com maior rating;
- envio de mensagem exige remetente vinculado ao chat;
- mensagem em chat finalizado é bloqueada;
- acesso a chats e mensagens é validado no backend por perfil/vínculo.

## Perfis de acesso
| Perfil | Escopo principal | Permissões |
|---|---|---|
| `ADMIN` | Gestão completa | Usuários, ajudantes, chats, status e administração geral |
| `USUÁRIO` | Atendimento próprio | Abre chat para si, acompanha e envia mensagens nos próprios chats |
| `AJUDANTE` | Atendimento vinculado | Acompanha e envia mensagens apenas nos chats vinculados ao próprio perfil |

Resumo de rotas web:
- `/app/usuarios/**` e `/app/ajudantes/**`: somente ADMIN;
- `/app/chats/novo`: ADMIN e USUÁRIO;
- `/app/chats/abrir` (POST): somente USUÁRIO;
- `/app/chats/*/status` e `/app/chats/*/excluir` (POST): somente ADMIN;
- `/app/chats/*/conversa/mensagens` (POST): USUÁRIO ou AJUDANTE.

## Acesso ao sistema em ambiente local
> Credenciais de uso **exclusivo** para ambiente local/desenvolvimento.  
> Não utilizar em produção.  
> Dependem da carga inicial Flyway (`V6`, `V7`, `V8`, `V9`).

| Perfil | Login | Senha |
|---|---|---|
| ADMIN | `admin` | `admin123` |
| USUÁRIO | `11999998888` | `demo12345` |
| AJUDANTE | `ajudante.escuta` | `apoio12345` |

Diretriz adotada:
- credenciais locais ficam somente no README;
- interface inicial não exibe logins/senhas de teste.

## Como executar o projeto
### Pré-requisitos
- Java 21+;
- Oracle Database (19c, 21c ou 23c);
- schema com permissão de criar/alterar objetos;
- Maven Wrapper (já no repositório).

### Variáveis obrigatórias
- `DB_URL` (ex.: `jdbc:oracle:thin:@localhost:1521/FREEPDB1`)
- `DB_USERNAME`
- `DB_PASSWORD`

### Variáveis opcionais
- `SPRING_PROFILES_ACTIVE` (default: `oracle`)
- `JPA_DDL_AUTO` (default: `validate`)
- `JPA_SHOW_SQL` (default: `false`)
- `SERVER_PORT` (default: `8080`)

### Subir no Windows (PowerShell)
```powershell
$env:SPRING_PROFILES_ACTIVE="oracle"
$env:DB_URL="jdbc:oracle:thin:@localhost:1521/FREEPDB1"
$env:DB_USERNAME="AIURY_APP"
$env:DB_PASSWORD="SUA_SENHA"
.\mvnw.cmd clean spring-boot:run
```

### Subir no Linux/macOS
```bash
export SPRING_PROFILES_ACTIVE=oracle
export DB_URL=jdbc:oracle:thin:@localhost:1521/FREEPDB1
export DB_USERNAME=AIURY_APP
export DB_PASSWORD=SUA_SENHA
./mvnw clean spring-boot:run
```

### URLs principais
- Home: `http://localhost:8080/`
- Login: `http://localhost:8080/login`
- Painel: `http://localhost:8080/app`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Fluxo rápido de avaliação
1. Subir aplicação com Oracle e Flyway concluído.
2. Entrar com perfil ADMIN.
3. Validar acesso ao painel e módulos administrativos.
4. Validar abertura/listagem de chats.
5. Validar conversa e envio de mensagens.
6. Validar bloqueio de acesso indevido para perfis não autorizados.
7. Validar documentação Swagger.

## Como testar o sistema
### Testes automatizados
```powershell
.\mvnw.cmd clean test
.\mvnw.cmd clean package
```

### Testes de integração Oracle (condicional)
```powershell
$env:ORACLE_TEST_ENABLED="true"
$env:TEST_DB_URL="jdbc:oracle:thin:@localhost:1521/FREEPDB1"
$env:TEST_DB_USERNAME="AIURY_TEST"
$env:TEST_DB_PASSWORD="SUA_SENHA"
.\mvnw.cmd test
```

### Validação via Postman
Use os artefatos em `docs/postman/` e siga o guia:
- [docs/postman/README.md](docs/postman/README.md)

## Configuração do ambiente
Arquivos principais:
- `src/main/resources/application.properties`
- `src/main/resources/application-oracle.properties`
- `src/main/resources/messages.properties`
- `src/main/resources/messages_pt_BR.properties`

Pontos importantes:
- profile padrão: `oracle`;
- Flyway habilitado por padrão;
- migrations em `classpath:db/migration/oracle`;
- projeto não usa `.env` nativo;
- projeto não possui Docker/Docker Compose neste estado.

## Estrutura do projeto
```text
src/main/java/br/com/fiap/aiury
|- controller/              # REST
|- controller/mvc/          # Fluxos de telas
|- services/                # Regras de negocio
|- repositories/            # JPA
|- entities/                # Modelo persistente
|- dto/                     # Contratos REST
|- dto/web/                 # Contratos de tela
|- mappers/                 # Conversoes
|- representation/          # HATEOAS
|- security/                # Login/perfis/autorizacao
|- configs/                 # Config global e OpenAPI
|- exceptions/              # Erros de dominio

src/main/resources
|- application*.properties
|- db/migration/oracle/
|- templates/
|- static/css/

docs/
|- arquitetura.md
|- modelagem.md
|- endpoints.md
|- testes.md
|- visao-mvp.md
|- postman/
```

## Fluxo de autenticação e autorização
### Login
- endpoint: `/login`;
- sessão via cookie `JSESSIONID`;
- senhas persistidas em BCrypt (sem fallback para texto puro);
- resolução de usuário em 3 fontes:
  - admin por `TB_ADMIN_ACCOUNT.NM_LOGIN`,
  - usuário por `TB_USUARIO.NR_CELULAR`,
  - ajudante por `TB_AJUDANTE.NM_LOGIN`;
- redirecionamento pós-login para `/app`.

### Autorização
- regras declarativas em `SecurityConfig`;
- reforço de segurança no backend (services), não apenas na UI;
- rotas não mapeadas explicitamente são negadas (`denyAll`).

### API x MVC
- MVC (`/app/**`): operação diária;
- REST (`/api/**`): integração técnica;
- GET de catálogos/API root para perfis autenticados;
- escrita administrativa restrita ao ADMIN, com exceções controladas para fluxo de mensagens.

## Banco de dados e Flyway
### Banco oficial
- Oracle Database.

### Migrations
- `V1` e `V2`: estrutura inicial + índices;
- `V3`: ajustes de segurança/remetente;
- `V4`: tabela administrativa;
- `V5`: padronização corporativa (`TB_*`, colunas, constraints e índices);
- `V6`, `V7`, `V8`: seed de referência, autenticação e dados de demonstração.
- `V9`: normalização de credenciais legadas em texto puro para BCrypt.

### Dados iniciais
- 2 estados, 3 cidades;
- 2 usuários, 2 ajudantes;
- 2 chats e 5 mensagens;
- 1 conta ADMIN.

### Reset local (recomendação)
1. usar schema dedicado de desenvolvimento;
2. recriar schema (ou limpar objetos de negócio + histórico Flyway);
3. subir a aplicação novamente para reaplicar migrations.

## Documentação da API
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- entrypoint HATEOAS: `GET /api`

Recursos documentados:
- payloads de criação/atualização;
- filtros por recurso;
- status HTTP e estrutura de erros.

## Boas práticas e arquitetura
- arquitetura em camadas com responsabilidades separadas;
- DTOs específicos para API e MVC;
- tratamento global de exceções;
- HATEOAS em recursos REST;
- validação de regra de acesso no backend;
- migrations versionadas e idempotentes para seed;
- `@EnableCaching` preservado na aplicação;
- `@OpenAPIDefinition` preservado na configuração OpenAPI.

## Melhorias futuras
- paginação e filtros avançados nas telas;
- trilha de auditoria de ações críticas;
- observabilidade com métricas/alertas;
- pipeline CI/CD com gates de qualidade mais rígidos;
- ampliação de testes de integração Oracle em ambiente dedicado.

## Documentação complementar
- [docs/arquitetura.md](docs/arquitetura.md)
- [docs/modelagem.md](docs/modelagem.md)
- [docs/endpoints.md](docs/endpoints.md)
- [docs/testes.md](docs/testes.md)
- [docs/visao-mvp.md](docs/visao-mvp.md)
- [docs/postman/README.md](docs/postman/README.md)
- [docs/imagens/README.md](docs/imagens/README.md)

## Integrantes
- **Renato De Angelo - RM 560585**
- **Nathália Mantovani de Falco - RM 99904**

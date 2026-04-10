# Aiury - Sistema de Gestao de Atendimentos (Disciplina Java)

Aplicacao Spring Boot com interface web (Thymeleaf) e API REST para gestao de atendimentos entre perfis `ADMIN`, `USUARIO` e `AJUDANTE`, com autenticacao por sessao, autorizacao por perfil/vinculo e persistencia Oracle com Flyway.

## Sumario
1. [Visao geral](#visao-geral)
2. [Problema que o sistema resolve](#problema-que-o-sistema-resolve)
3. [Objetivos do projeto](#objetivos-do-projeto)
4. [Tecnologias principais](#tecnologias-principais)
5. [Perfis de acesso](#perfis-de-acesso)
6. [Fluxos principais do sistema](#fluxos-principais-do-sistema)
7. [Como executar o projeto](#como-executar-o-projeto)
8. [Acesso local ao sistema](#acesso-local-ao-sistema)
9. [URLs importantes](#urls-importantes)
10. [Testes e evidencias](#testes-e-evidencias)
11. [Diagramas e documentacao complementar](#diagramas-e-documentacao-complementar)
12. [Estrutura do projeto](#estrutura-do-projeto)
13. [Video de demonstracao](#video-de-demonstracao)
14. [Integrantes](#integrantes)

## Visao geral
O Aiury organiza o ciclo de atendimento de ponta a ponta:
- cadastro e manutencao de usuarios e ajudantes;
- abertura, acompanhamento e finalizacao de chats;
- envio de mensagens com historico rastreavel;
- regras de acesso reforcadas no backend por perfil e vinculo real.

## Problema que o sistema resolve
O projeto reduz riscos comuns em atendimento digital:
- atendimento sem historico confiavel;
- acesso indevido a dados de terceiros;
- regras de negocio inconsistentes entre tela e backend.

## Objetivos do projeto
### Objetivo funcional
Entregar um sistema de atendimento com operacao web e API, com regras claras para chats e mensagens, incluindo controle de acesso por perfil.

### Objetivo tecnico
Consolidar uma entrega madura para as Sprints 1, 2 e 3 da disciplina de Java com:
- arquitetura em camadas;
- Spring Security com autorizacao real no backend;
- JPA/Hibernate com Oracle;
- Flyway para migracoes e seed;
- OpenAPI/Swagger;
- testes automatizados e roteiros de validacao.

## Tecnologias principais
- Java 21+ (projeto tambem compila em Java mais recente no ambiente local atual)
- Spring Boot 3.5.6
- Spring Web + Thymeleaf
- Spring Security
- Spring Data JPA + Hibernate
- Flyway
- Oracle Database
- Spring HATEOAS
- springdoc OpenAPI / Swagger UI
- JUnit 5 + Mockito

## Perfis de acesso
| Perfil | Escopo principal | Permissoes principais |
|---|---|---|
| `ADMIN` | Gestao da plataforma | Gerencia usuarios, ajudantes, chats e operacoes administrativas |
| `USUARIO` | Atendimento proprio | Abre chat para si, acompanha conversas e envia mensagens dos proprios chats |
| `AJUDANTE` | Atendimento vinculado | Acessa e responde apenas chats vinculados ao proprio perfil |

## Fluxos principais do sistema
### Fluxo web (MVC)
- Login em `/login`
- Painel inicial em `/app` com atalhos por perfil
- Gestao de usuarios (`/app/usuarios`) e ajudantes (`/app/ajudantes`) para `ADMIN`
- Operacao de chats (`/app/chats`) e conversa (`/app/chats/{id}/conversa`)
- Minha conta (`/app/minha-conta`) para troca de senha

### Fluxo REST
- Entrypoint HATEOAS: `GET /api`
- Recursos principais: `estados`, `cidades`, `usuarios`, `ajudantes`, `chats`, `mensagens`
- Swagger/OpenAPI para inspecao de contratos, payloads e respostas

### Regras de negocio relevantes
- usuario nao pode manter mais de um chat ativo ao mesmo tempo;
- abertura de chat de usuario escolhe ajudante disponivel com melhor rating;
- envio de mensagem exige remetente valido no contexto do chat;
- envio em chat finalizado e bloqueado;
- acesso a chat/mensagem e validado no backend (nao apenas pela UI).

## Como executar o projeto
### Pre-requisitos
- Java 21+
- Oracle Database (19c, 21c ou 23c)
- Schema com permissao para criar/alterar objetos
- Maven Wrapper (ja no repositorio)

### Variaveis obrigatorias
- `DB_URL` (exemplo: `jdbc:oracle:thin:@localhost:1521/FREEPDB1`)
- `DB_USERNAME`
- `DB_PASSWORD`

### Variaveis opcionais
- `SPRING_PROFILES_ACTIVE` (default: `oracle`)
- `JPA_DDL_AUTO` (default: `validate`)
- `JPA_SHOW_SQL` (default: `false`)
- `FLYWAY_BASELINE_ON_MIGRATE` (default: `true`)
- `FLYWAY_BASELINE_VERSION` (default: `0`)
- `SERVER_PORT` (default: `8080`)

### Execucao no Windows (PowerShell)
```powershell
$env:SPRING_PROFILES_ACTIVE="oracle"
$env:DB_URL="jdbc:oracle:thin:@localhost:1521/FREEPDB1"
$env:DB_USERNAME="AIURY_APP"
$env:DB_PASSWORD="SUA_SENHA"
.\mvnw.cmd clean spring-boot:run
```

### Execucao no Linux/macOS
```bash
export SPRING_PROFILES_ACTIVE=oracle
export DB_URL=jdbc:oracle:thin:@localhost:1521/FREEPDB1
export DB_USERNAME=AIURY_APP
export DB_PASSWORD=SUA_SENHA
./mvnw clean spring-boot:run
```

## Acesso local ao sistema
Credenciais de uso exclusivo para desenvolvimento/local (dependem das migracoes Flyway `V6` a `V9`):

| Perfil | Login | Senha |
|---|---|---|
| `ADMIN` | `admin` | `admin123` |
| `USUARIO` | `11999998888` | `demo12345` |
| `AJUDANTE` | `ajudante.escuta` | `apoio12345` |

Observacao:
- essas credenciais nao devem ser usadas em producao;
- a interface inicial nao exibe logins/senhas de teste.

## URLs importantes
- Aplicacao: `http://localhost:8080/`
- Login: `http://localhost:8080/login`
- Painel: `http://localhost:8080/app`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Testes e evidencias
### Testes automatizados
```powershell
.\mvnw.cmd clean test
.\mvnw.cmd clean package
```

### Testes Oracle de integracao (condicional)
```powershell
$env:ORACLE_TEST_ENABLED="true"
$env:TEST_DB_URL="jdbc:oracle:thin:@localhost:1521/FREEPDB1"
$env:TEST_DB_USERNAME="AIURY_TEST"
$env:TEST_DB_PASSWORD="SUA_SENHA"
.\mvnw.cmd test
```

### Onde estao os testes no repositorio
- `src/test/java/.../services`
- `src/test/java/.../controller`
- `src/test/java/.../security`
- `src/test/java/.../repositories`

### Collection para validacao de endpoints
- `docs/postman/Aiury-Sprint3.postman_collection.json`
- `docs/postman/Aiury-local.postman_environment.json`
- Guia: `docs/postman/README.md`

## Diagramas e documentacao complementar
### Diagramas versionaveis (fonte)
- DER: `docs/diagramas/der.mmd`
- Diagrama de classes: `docs/diagramas/diagrama-classes.puml`
- Arquitetura: `docs/diagramas/arquitetura.mmd`

### Exportacao visual (PNG)
- Arquivos gerados nesta entrega:
  - `docs/imagens/der.png`
  - `docs/imagens/diagrama-classes.png`
  - `docs/imagens/arquitetura.png`
- Procedimento de geracao/regeracao: `docs/imagens/README.md`

### Documentacao tecnica
- `docs/arquitetura.md`
- `docs/modelagem.md`
- `docs/endpoints.md`
- `docs/testes.md`
- `docs/visao-mvp.md`
- `docs/cronograma.md`
- `docs/postman/README.md`
- `docs/imagens/README.md`

## Estrutura do projeto
```text
src/main/java/br/com/fiap/aiury
|- controller/              # REST
|- controller/mvc/          # Fluxos web
|- services/                # Regras de negocio
|- repositories/            # Persistencia JPA
|- entities/                # Entidades de dominio
|- dto/                     # Contratos REST
|- dto/web/                 # Formularios e visoes MVC
|- mappers/                 # Conversoes
|- representation/          # HATEOAS
|- security/                # Autenticacao e autorizacao
|- configs/                 # Configuracoes globais/OpenAPI
|- exceptions/              # Tratamento de erros

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
|- cronograma.md
|- diagramas/
|- imagens/
|- postman/
```

## Video de demonstracao
- Status do link final: **PENDENTE**
- Link para publicar na entrega final: `INSERIR_LINK_YOUTUBE_AQUI`

## Integrantes
| Nome completo | RM | Breve apresentacao | Responsabilidade principal no projeto |
|---|---|---|---|
| Renato De Angelo | RM 560585 | Integrante com atuacao tecnica central no backend e na organizacao da entrega. | Implementacao de backend/modelagem (entidades, services, repositories), evolucao de API e consolidacao da documentacao tecnica. |
| Nathália Mantovani de Falco | RM 99904 | Integrante da equipe para validacao final da entrega academica. | Apoio em evidencias de demonstracao e revisao final de consistencia da entrega (conforme frente de equipe no cronograma). |

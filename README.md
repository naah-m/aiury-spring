# Aiury - Sistema de Gestão de Atendimentos

Aplicação web e API REST para gestão de atendimentos entre **USUÁRIOS** e **AJUDANTES**, com controle de acesso por perfil, persistência relacional em Oracle e migrações versionadas.

## Sumário
1. [Visão geral](#visão-geral)
2. [Objetivos do projeto](#objetivos-do-projeto)
3. [Funcionalidades principais](#funcionalidades-principais)
4. [Perfis de acesso](#perfis-de-acesso)
5. [Acesso ao sistema em ambiente local](#acesso-ao-sistema-em-ambiente-local)
6. [Como executar o projeto](#como-executar-o-projeto)
7. [Configuração do ambiente](#configuração-do-ambiente)
8. [Estrutura do projeto](#estrutura-do-projeto)
9. [Fluxo de autenticação e autorização](#fluxo-de-autenticação-e-autorização)
10. [Banco de dados](#banco-de-dados)
11. [Documentação da API](#documentação-da-api)
12. [Boas práticas e arquitetura](#boas-práticas-e-arquitetura)
13. [Melhorias futuras](#melhorias-futuras)
14. [Integrantes](#integrantes)

## Visão geral
O Aiury organiza o fluxo de atendimento em três frentes principais:
- cadastro e manutenção de pessoas e catálogos de referência (estados e cidades);
- abertura e acompanhamento de chats de atendimento;
- troca de mensagens com regras de vínculo entre participante e chat.

Problema que o sistema resolve:
- evitar atendimento sem rastreabilidade;
- padronizar controle de acesso por perfil;
- oferecer visão operacional web e API para integração e auditoria funcional.

Fluxos de negócio principais:
- **ADMIN** administra usuários, ajudantes e operação de chats;
- **USUÁRIO** abre chat e acompanha seus atendimentos;
- **AJUDANTE** acompanha e responde atendimentos vinculados ao seu perfil.

## Objetivos do projeto
### Objetivo funcional
Disponibilizar um sistema de atendimento com autenticação por perfil, gestão de dados de domínio e operação de chats/mensagens com regras de acesso consistentes.

### Objetivo técnico
Entregar uma base sustentada por:
- Spring Boot 3;
- Spring Security;
- Spring Data JPA + Hibernate;
- Thymeleaf (camada web MVC);
- Flyway para versionamento de banco;
- OpenAPI/Swagger para documentação da API.

## Funcionalidades principais
### Módulo web (MVC)
- login e logout com redirecionamento para painel inicial (`/app`);
- dashboard com atalhos por perfil e resumo operacional;
- gestão de usuários (`/app/usuarios`) para perfil ADMIN;
- gestão de ajudantes (`/app/ajudantes`) para perfil ADMIN;
- gestão de chats (`/app/chats`) com filtros e detalhes;
- abertura de novo chat pelo usuário (`/app/chats/novo`);
- conversa do chat com envio de mensagem (`/app/chats/{id}/conversa`);
- área de minha conta com troca de senha (`/app/minha-conta`).

### API REST
- recursos versionados via `/api` com HATEOAS:
  - `estados`
  - `cidades`
  - `usuarios`
  - `ajudantes`
  - `chats`
  - `mensagens`
- documentação OpenAPI disponível localmente.

### Regras de negócio implementadas
- um usuário não pode manter mais de um chat ativo ao mesmo tempo;
- abertura de chat do usuário escolhe ajudante disponível com melhor rating;
- mensagens só podem ser enviadas por participante vinculado ao chat;
- não é permitido enviar mensagem em chat finalizado;
- validações de integridade, unicidade e consistência temporal aplicadas na camada de serviço.

## Perfis de acesso
| Perfil | Escopo principal | Permissões relevantes |
|---|---|---|
| `ADMIN` | Gestão completa | Administra usuários, ajudantes, chats e pode alterar status/excluir chat |
| `USUÁRIO` | Atendimento próprio | Abre chat para si, visualiza e envia mensagens apenas nos próprios chats |
| `AJUDANTE` | Atendimento vinculado | Visualiza e envia mensagens apenas nos chats vinculados ao próprio perfil |

Resumo de autorização:
- `/app/usuarios/**` e `/app/ajudantes/**`: somente `ADMIN`;
- `/app/chats/novo`: `ADMIN` e `USUÁRIO`;
- `/app/chats/abrir` (POST): somente `USUÁRIO`;
- `/app/chats/*/status` e `/app/chats/*/excluir` (POST): somente `ADMIN`;
- `/app/chats/*/conversa/mensagens` (POST): `USUÁRIO` ou `AJUDANTE`;
- `/app/minha-conta/**`: todos os perfis autenticados.

## Acesso ao sistema em ambiente local
> Uso exclusivo para desenvolvimento/local.
> Não utilizar em produção.
> Credenciais dependem da carga inicial do banco via Flyway (migrations `V6`, `V7` e `V8`).

| Perfil | Login | Senha |
|---|---|---|
| ADMIN | `admin` | `admin123` |
| USUÁRIO | `11999998888` | `demo12345` |
| AJUDANTE | `ajudante.escuta` | `apoio12345` |

Diretriz do projeto:
- as credenciais de ambiente local devem ficar documentadas no README;
- a interface inicial não exibe credenciais de teste.

## Como executar o projeto
## Pré-requisitos
- Java 21+;
- Oracle Database acessível (19c, 21c ou 23c);
- Maven Wrapper (já incluído no projeto);
- schema com permissão para criar/alterar objetos.

## Variáveis de ambiente obrigatórias
- `DB_URL`  
  Exemplo: `jdbc:oracle:thin:@localhost:1521/FREEPDB1`
- `DB_USERNAME`
- `DB_PASSWORD`

## Variáveis opcionais
- `SPRING_PROFILES_ACTIVE` (default: `oracle`)
- `JPA_DDL_AUTO` (default: `validate`)
- `JPA_SHOW_SQL` (default: `false`)
- `SERVER_PORT` (default: `8080`)

## Executar no Windows (PowerShell)
```powershell
$env:SPRING_PROFILES_ACTIVE="oracle"
$env:DB_URL="jdbc:oracle:thin:@localhost:1521/FREEPDB1"
$env:DB_USERNAME="AIURY_APP"
$env:DB_PASSWORD="SUA_SENHA"
.\mvnw.cmd clean spring-boot:run
```

## Executar no Linux/macOS
```bash
export SPRING_PROFILES_ACTIVE=oracle
export DB_URL=jdbc:oracle:thin:@localhost:1521/FREEPDB1
export DB_USERNAME=AIURY_APP
export DB_PASSWORD=SUA_SENHA
./mvnw clean spring-boot:run
```

## Endereços locais padrão
- Home: `http://localhost:8080/`
- Login: `http://localhost:8080/login`
- Painel: `http://localhost:8080/app`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Configuração do ambiente
Arquivos de configuração:
- `src/main/resources/application.properties`
- `src/main/resources/application-oracle.properties`
- `src/main/resources/messages.properties`
- `src/main/resources/messages_pt_BR.properties`

Observações:
- profile padrão da aplicação: `oracle`;
- Flyway habilitado por padrão;
- localização de migration: `classpath:db/migration/oracle`;
- não há `.env` nativo no projeto (uso via variáveis de ambiente do sistema);
- não há Docker/Docker Compose no estado atual do repositório.

Primeira execução:
1. Configurar variáveis obrigatórias de banco.
2. Subir a aplicação.
3. Validar logs de migração Flyway concluída.
4. Acessar `/login` e autenticar com as credenciais locais desta documentação.

## Estrutura do projeto
```text
src/main/java/br/com/fiap/aiury
|- controller/              # REST controllers
|- controller/mvc/          # Controllers de telas Thymeleaf
|- services/                # Regras de negócio
|- repositories/            # Spring Data JPA
|- entities/                # Entidades JPA
|- dto/                     # DTOs de API
|- dto/web/                 # DTOs para views MVC
|- mappers/                 # Conversões DTO <-> entidade
|- mappers/web/             # Conversões para telas MVC
|- representation/          # Builders HATEOAS
|- configs/                 # Configurações globais (OpenAPI, Jackson, erros)
|- security/                # Segurança, principal, sucesso de login
|- exceptions/              # Exceções de domínio

src/main/resources
|- application*.properties
|- db/migration/oracle/     # Migrations Flyway V1..V8
|- templates/               # Thymeleaf (layout, fragments, telas)
|- static/css/              # Estilos da aplicação

docs/
|- endpoints.md
|- modelagem.md
|- arquitetura.md
|- testes.md
|- postman/
```

## Fluxo de autenticação e autorização
## Autenticação (login)
- endpoint web: `/login`;
- mecanismo: formulário Spring Security;
- origem da credencial:
  - `ADMIN`: tabela `TB_ADMIN_ACCOUNT`, login por `NM_LOGIN`;
  - `USUÁRIO`: tabela `TB_USUARIO`, login por `NR_CELULAR`;
  - `AJUDANTE`: tabela `TB_AJUDANTE`, login por `NM_LOGIN`;
- após autenticação sem `savedRequest`, redireciona para `/app`.

## Autorização
- baseada em roles (`ROLE_ADMIN`, `ROLE_USUARIO`, `ROLE_AJUDANTE`);
- políticas declaradas em `SecurityConfig`;
- validações de acesso também existem na camada de serviço para impedir acesso indevido por ID.

## MVC x API protegida
- camada MVC (`/app/**`): experiência operacional da interface web;
- camada API (`/api/**`): endpoints REST com regras por método/perfil;
- GETs de catálogo e navegação HATEOAS exigem usuário autenticado;
- operações de escrita em recursos administrativos são restritas a ADMIN.

## Relação entre usuário, ajudante, chats e vínculos
- chat possui `usuario` e `ajudante` obrigatórios;
- mensagem pertence a um chat e tem remetente exclusivo (usuário ou ajudante);
- usuário/ajudante só visualiza e manipula dados vinculados ao próprio contexto.

## Banco de dados
Banco oficial:
- Oracle Database.

Migrations Flyway:
- `V1` e `V2`: criação inicial de tabelas e índices;
- `V3`: ajustes de segurança e remetente de mensagem;
- `V4`: conta administrativa;
- `V5`: padronização corporativa de nomenclatura (`TB_*`, colunas, constraints, índices);
- `V6`, `V7`, `V8`: seed de referência, autenticação e dados de demonstração.

Dados iniciais de demonstração:
- 2 estados;
- 3 cidades;
- 2 usuários;
- 2 ajudantes;
- 2 chats;
- 5 mensagens;
- 1 conta admin.

## Reset/recriação da base local
Estratégia recomendada:
1. usar um schema dedicado de desenvolvimento;
2. recriar o schema (ou remover objetos de negócio + histórico Flyway);
3. subir a aplicação novamente para reaplicar migrations do zero.

Boas práticas adotadas:
- migrations idempotentes para seed;
- constraints explícitas para integridade;
- nomenclatura padronizada de objetos Oracle.

## Documentação da API
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- root HATEOAS da API: `GET /api`

Recursos disponíveis na API:
- estados, cidades, usuários, ajudantes, chats e mensagens;
- documentação com exemplos de payload e erros.

Diferenciação:
- interface web = operação do dia a dia;
- API REST = integração, testes e automação.

Material de apoio:
- [docs/endpoints.md](docs/endpoints.md)
- [docs/modelagem.md](docs/modelagem.md)
- [docs/arquitetura.md](docs/arquitetura.md)
- [docs/testes.md](docs/testes.md)
- coleção Postman em `docs/postman/`.

## Boas práticas e arquitetura
- separação por camadas (`controller`, `service`, `repository`, `entity`);
- DTO de request/response com mapeamento dedicado;
- tratamento global de exceções com payload padronizado;
- HATEOAS nos recursos REST;
- regras de autorização aplicadas em configuração de segurança e reforçadas no serviço;
- migração de banco versionada com Flyway;
- interface MVC com layout base e fragments reutilizáveis.

## Melhorias futuras
- paginação e ordenação avançada em listas web/API;
- trilha de auditoria de ações críticas;
- observabilidade com métricas e health checks expandidos;
- pipeline CI/CD com validações automatizadas mais amplas;
- fortalecimento de testes de integração Oracle em ambiente dedicado.

## Integrantes
- **Renato de Angelo**
- **Nathália Mantovani de Falco — RM 99904**

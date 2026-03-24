# Aiury - API Backend de Acolhimento

API REST em Java/Spring Boot para suporte ao fluxo de acolhimento entre pessoas usuarias e ajudantes.  
O projeto implementa o nucleo de cadastro e interacao do MVP, com persistencia relacional, validacoes de entrada e documentacao OpenAPI.  
O foco atual esta em operacoes CRUD para os recursos centrais do dominio.

## 1. Visao geral

### Proposito do projeto
O projeto Aiury busca estruturar tecnicamente uma base de atendimento e acolhimento digital, permitindo registrar participantes, sessoes de conversa e mensagens trocadas.

### Problema que o sistema busca resolver
Sem uma base estruturada, o acompanhamento de interacoes de acolhimento tende a ficar descentralizado, dificultando rastreabilidade, organizacao de dados e evolucao de funcionalidades.

### Proposta de solucao (MVP atual)
A solucao implementada nesta fase oferece uma API backend para:
- cadastro e manutencao de usuarios e ajudantes;
- abertura e atualizacao de chats de acolhimento;
- registro de mensagens vinculadas aos chats;
- persistencia dos dados em banco relacional Oracle.

### Visao completa x recorte atual
A visao completa do produto pode incluir recursos avancados (autenticacao, canais em tempo real, analytics etc.), mas o recorte atual implementado esta concentrado no backend CRUD do dominio principal.

## 2. Objetivos do projeto

### Objetivo geral
Desenvolver uma API REST organizada em camadas para sustentar o fluxo basico de acolhimento digital entre usuarios e ajudantes.

### Objetivos especificos
- Estruturar o dominio com entidades e relacionamentos coerentes.
- Implementar endpoints REST para os recursos principais do MVP.
- Aplicar validacoes de entrada e tratamento padrao de erros.
- Disponibilizar documentacao tecnica para evolucao por sprint e entrega academica.

## 3. Escopo atual do MVP

No estado atual do backend, estao implementados:
- CRUD de `Usuario` (com resposta HATEOAS nos endpoints de usuario).
- CRUD de `Ajudante`.
- CRUD de `Chat` com vinculacao por `usuarioId` e `ajudanteId`.
- CRUD de `Mensagem` com vinculacao por `chatId` e `remetenteId`.
- Relacionamentos de localizacao via `Cidade` e `Estado`.
- Tratamento global de erro para validacao (`400`) e recurso nao encontrado (`404`).

Nao fazem parte deste recorte atual:
- autenticacao/autorizacao;
- paginacao e filtros avancados;
- comunicacao em tempo real (websocket);
- regras avancadas de workflow.

## 4. Tecnologias utilizadas

Tecnologias confirmadas no projeto:
- Java 21
- Spring Boot 3.5.6
- Spring Web
- Spring Data JPA
- Spring Validation (Bean Validation)
- Spring HATEOAS
- Springdoc OpenAPI (Swagger UI)
- Lombok
- Oracle JDBC (`ojdbc11`)
- Maven (com Maven Wrapper)
- JUnit 5 / Spring Boot Test

## 5. Estrutura do projeto

Organizacao principal em camadas/pacotes:

```text
src/
  main/
    java/br/com/fiap/aiury/
      configs/        # tratamento global e configuracoes da API
      controller/     # endpoints REST
      dto/            # contratos de entrada/saida da API
      entities/       # entidades JPA e enum de dominio
      exceptions/     # excecoes de negocio
      mappers/        # conversao DTO <-> entidade
      repositories/   # acesso a dados (Spring Data JPA)
      services/       # interfaces e implementacoes de regras de servico
      AiuryApplication.java
    resources/
      application.properties
  test/
    java/br/com/fiap/aiury/
```

## 6. Modelagem de dominio

Entidades e papeis principais:

| Entidade | Papel no dominio | Relacionamentos basicos |
|---|---|---|
| `Usuario` | Pessoa usuaria da plataforma | `ManyToOne` com `Cidade`; referenciado por `Chat` e `Mensagem` |
| `Ajudante` | Perfil de acolhimento | Referenciado por `Chat` |
| `Chat` | Sessao de acolhimento | `ManyToOne` com `Usuario`; `ManyToOne` com `Ajudante`; `OneToMany` com `Mensagem` |
| `Mensagem` | Registro textual da conversa | `ManyToOne` com `Chat`; `ManyToOne` com `Usuario` (remetente) |
| `Cidade` | Localizacao municipal | `ManyToOne` com `Estado`; referenciada por `Usuario` |
| `Estado` | Unidade federativa | Referenciado por `Cidade` |

Enum de dominio:
- `ChatStatus`: `INICIADO`, `EM_ANDAMENTO`, `FINALIZADO_USUARIO`, `FINALIZADO_AJUDANTE`, `FINALIZADO_SISTEMA`.

Artefatos visuais (inserir na entrega):
- DER: `[INSERIR_IMAGEM_DER]`
- Diagrama de classes: `[INSERIR_IMAGEM_DIAGRAMA_CLASSES]`

## 7. Endpoints da API

Resumo dos recursos expostos atualmente:

### Usuario (`/api/usuarios`)
- `POST /api/usuarios`
- `GET /api/usuarios`
- `GET /api/usuarios/{id}`
- `PUT /api/usuarios/{id}`
- `DELETE /api/usuarios/{id}`

### Ajudante (`/api/ajudantes`)
- `POST /api/ajudantes`
- `GET /api/ajudantes`
- `GET /api/ajudantes/{id}`
- `PUT /api/ajudantes/{id}`
- `DELETE /api/ajudantes/{id}`

### Chat (`/api/chats`)
- `POST /api/chats`
- `GET /api/chats`
- `GET /api/chats/{id}`
- `PUT /api/chats/{id}`
- `DELETE /api/chats/{id}`

### Mensagem (`/api/mensagens`)
- `POST /api/mensagens`
- `GET /api/mensagens`
- `GET /api/mensagens/{id}`
- `PUT /api/mensagens/{id}`
- `DELETE /api/mensagens/{id}`

Documentacao detalhada (payload e respostas):
- `docs/endpoints.md`

Swagger/OpenAPI (rota padrao do springdoc):
- UI: `http://localhost:8080/swagger-ui/index.html`
- JSON OpenAPI: `http://localhost:8080/v3/api-docs`

Se houver customizacao futura de contexto/porta, atualizar os links acima.

## 8. Como executar o projeto localmente

### Pre-requisitos
- JDK 21
- Maven 3.9+ (opcional, pois o projeto usa `mvnw`)
- Acesso ao Oracle Database (ou ambiente equivalente configurado)

### Configuracao de banco
Editar o arquivo:
- `src/main/resources/application.properties`

Campos principais:
- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`
- `spring.jpa.hibernate.ddl-auto` (atualmente `update`)

### Passo a passo
1. Clonar o repositorio.
2. Ajustar `application.properties` para o seu ambiente.
3. Executar a aplicacao:
   - Windows:
     ```bash
     .\mvnw.cmd spring-boot:run
     ```
   - Linux/macOS:
     ```bash
     ./mvnw spring-boot:run
     ```
4. Validar subida da API na porta configurada (atual: `8080`).
5. Acessar Swagger UI para exploracao de endpoints.

### Build e empacotamento
```bash
./mvnw clean package
```

### Observacao sobre encoding
Manter o ambiente em UTF-8 para evitar problemas com acentuacao em logs e documentacao.

## 9. Testes e validacao

### Testes automatizados
Executar:
```bash
./mvnw test
```

No estado atual, o projeto possui teste de contexto (`contextLoads`) como validacao inicial de bootstrap.

### Testes manuais da API
Ferramentas recomendadas:
- Swagger UI
- Postman / Insomnia

Colecao de testes:
- `[INSERIR_LINK_OU_CAMINHO_POSTMAN]`

## 10. Evolucao por sprint

Resumo alinhado aos documentos atuais do projeto:

| Sprint | Evolucao principal | Status (atual) |
|---|---|---|
| Sprint 1 | Modelagem inicial do dominio (`Usuario`, `Ajudante`, `Chat`, `Mensagem`) e ajustes JPA | Concluido |
| Sprint 2 | Implementacao/expansao do backend (controllers, services, repositories, endpoints do MVP) | Em andamento |
| Sprint 3 | Consolidacao de documentacao final e materiais de apresentacao | Nao iniciado |

Referencia:
- `docs/cronograma.md`

## 11. Integrantes e responsabilidades

| Integrante | RM/Identificacao | Responsabilidade |
|---|---|---|
| [INSERIR_NOME_INTEGRANTE] | [INSERIR_RM] | [INSERIR_RESPONSABILIDADE] |
| [INSERIR_NOME_INTEGRANTE] | [INSERIR_RM] | [INSERIR_RESPONSABILIDADE] |
| [INSERIR_NOME_INTEGRANTE] | [INSERIR_RM] | [INSERIR_RESPONSABILIDADE] |
| [INSERIR_NOME_INTEGRANTE] | [INSERIR_RM] | [INSERIR_RESPONSABILIDADE] |

## 12. Artefatos da entrega

Checklist de artefatos:
- [ ] Video de apresentacao: `[INSERIR_LINK_VIDEO]`
- [ ] DER: `[INSERIR_IMAGEM_DER]`
- [ ] Diagrama de classes: `[INSERIR_IMAGEM_DIAGRAMA_CLASSES]`
- [ ] Documentacao de endpoints: `docs/endpoints.md`
- [ ] Colecao Postman/Insomnia: `[INSERIR_LINK_OU_CAMINHO_POSTMAN]`
- [ ] Swagger UI: `http://localhost:8080/swagger-ui/index.html`

## 13. Melhorias futuras

Evolucoes coerentes para proximas sprints:
- Implementar autenticacao/autorizacao (ex.: JWT + Spring Security).
- Incluir paginacao, ordenacao e filtros nos endpoints de consulta.
- Estruturar testes automatizados de integracao para fluxos de negocio.
- Padronizar contratos de resposta (incluindo metadados) para todos os recursos.
- Evoluir observabilidade (logs estruturados e metricas).

## 14. Conclusao

O projeto Aiury encontra-se em um estagio funcional de MVP backend, com dominio principal modelado e endpoints REST implementados para os recursos centrais de acolhimento.  
A base atual permite evolucao incremental com baixo acoplamento entre camadas, mantendo consistencia para continuidade academica e tecnica das proximas sprints.

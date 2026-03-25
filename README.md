# Aiury API - Plataforma de Acolhimento Emocional

API REST desenvolvida em Spring Boot para gerenciar usuarios, ajudantes, chats e mensagens em um fluxo de acolhimento digital com rastreabilidade, padronizacao de erro e navegacao HATEOAS.

## 1. Visao Geral
O projeto implementa um backend em arquitetura em camadas para suportar o ciclo de atendimento da plataforma Aiury, com persistencia relacional, validacao de dados, documentacao OpenAPI e testes automatizados.

## 2. Problema de Negocio
Processos de acolhimento informal tendem a perder historico, dificultar auditoria e comprometer continuidade de atendimento. O projeto resolve isso estruturando o fluxo em recursos REST versionaveis e consultaveis.

## 3. Objetivo da Solucao
- Centralizar cadastro e manutencao de usuarios e ajudantes.
- Controlar abertura, evolucao e encerramento de chats.
- Registrar mensagens por chat com trilha temporal.
- Entregar API documentada e testavel para avaliacao academica e consumo futuro por frontend/mobile.

## 4. Publico-Alvo
- Equipe academica (avaliacao de sprints).
- Equipe tecnica de backend/frontend.
- Consumidores futuros da API de acolhimento.

## 5. Arquitetura da Aplicacao
Arquitetura em camadas com responsabilidades claras:
- `controller`: contratos HTTP, status code e validacao de entrada.
- `service`: regras de negocio, validacao de existencia e orquestracao.
- `repository`: persistencia com Spring Data JPA.
- `entity`: modelo relacional JPA.
- `dto`: contratos `Request` e `Response`.
- `mappers`: conversao entre entidade e DTO.
- `representation`: composicao HATEOAS sem poluir controllers.
- `configs` e `exceptions`: OpenAPI e tratamento global de erros.

Documento detalhado: `docs/arquitetura.md`.

## 6. Tecnologias Utilizadas
- Java 21+
- Spring Boot 3.5.6
- Spring Web
- Spring Data JPA
- Spring Validation
- Spring HATEOAS
- Springdoc OpenAPI (Swagger UI)
- Maven Wrapper
- Oracle JDBC (`ojdbc11`)
- H2 (ambiente de testes)
- JUnit 5 e Mockito

## 7. Estrutura do Projeto
```text
src/main/java/br/com/fiap/aiury
  ├─ controller
  ├─ services
  ├─ repositories
  ├─ entities
  ├─ dto
  ├─ mappers
  ├─ representation
  ├─ configs
  └─ exceptions

src/test/java/br/com/fiap/aiury
  ├─ controller
  ├─ services
  └─ repositories

docs
  ├─ arquitetura.md
  ├─ endpoints.md
  ├─ modelagem.md
  ├─ cronograma.md
  ├─ testes.md
  └─ postman/
```

## 8. Requisitos para Execucao
- JDK 21 ou superior.
- Maven 3.9+ (opcional, pois o projeto inclui `mvnw`).
- Banco Oracle acessivel para execucao da aplicacao principal.

## 9. Como Configurar Banco de Dados
As credenciais nao ficam no repositorio. Configure variaveis de ambiente:
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JPA_DDL_AUTO` (opcional, default `update`)
- `JPA_SHOW_SQL` (opcional, default `false`)

Exemplo PowerShell:
```powershell
$env:DB_URL="jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl"
$env:DB_USERNAME="SEU_USUARIO_ORACLE"
$env:DB_PASSWORD="SUA_SENHA_ORACLE"
$env:JPA_DDL_AUTO="update"
```

## 10. Como Executar a Aplicacao
```powershell
.\mvnw.cmd spring-boot:run
```

## 11. Como Acessar Swagger
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- Atalho: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## 12. Endpoints Principais
| Recurso | Base path | Operacoes |
|---|---|---|
| Usuarios | `/api/usuarios` | `GET`, `GET/{id}`, `POST`, `PUT/{id}`, `DELETE/{id}` |
| Ajudantes | `/api/ajudantes` | `GET`, `GET/{id}`, `POST`, `PUT/{id}`, `DELETE/{id}` |
| Chats | `/api/chats` | `GET`, `GET/{id}`, `POST`, `PUT/{id}`, `DELETE/{id}` |
| Mensagens | `/api/mensagens` | `GET`, `GET/{id}`, `POST`, `PUT/{id}`, `DELETE/{id}` |

Filtros implementados:
- `/api/usuarios?cidadeId=`
- `/api/ajudantes?disponivel=`
- `/api/chats?usuarioId=&ajudanteId=&status=`
- `/api/mensagens?chatId=&remetenteId=`

Detalhamento completo: `docs/endpoints.md`.

## 13. Modelagem do Sistema
- Entidades principais: `Usuario`, `Ajudante`, `Chat`, `Mensagem`, `Cidade`, `Estado`.
- Relacionamentos mapeados via JPA com FKs explicitas.
- Consistencia entre entidades, repositories e DTOs validada por testes.

Documento detalhado: `docs/modelagem.md`.

## 14. DER
Imagem esperada em: `docs/imagens/der.png`.

## 15. Diagrama de Classes
Imagem esperada em: `docs/imagens/diagrama-classes.png`.

## 16. Testes da API
### Automatizados
```powershell
.\mvnw.cmd clean test
.\mvnw.cmd clean package
```

### Manuais (Postman)
- Collection: `docs/postman/Aiury-Sprint3.postman_collection.json`
- Environment: `docs/postman/Aiury-local.postman_environment.json`
- Guia: `docs/postman/README.md`

Documentacao de validacao: `docs/testes.md`.

## 17. Integrantes do Grupo
Preencher tabela no bloco final "Informacoes a complementar pelo grupo".

## 18. Funcao/Responsabilidade de Cada Integrante
Detalhar no bloco final por frente de trabalho:
- backend e persistencia;
- documentacao e arquitetura;
- testes e evidencias;
- apresentacao e consolidacao final.

## 19. Link do Video
Registrar no bloco final "Informacoes a complementar pelo grupo".

## 20. Melhorias Futuras
- Autenticacao/autorizacao com JWT.
- Auditoria de alteracoes em recursos criticos.
- Cobertura de testes de integracao para todos os controllers.
- Pipeline CI para build/test/package em pull request.
- Observabilidade (metricas e tracing) para acompanhamento operacional.

## 21. Conclusao
O repositorio esta estruturado para entrega academica de Sprint 3 com foco em robustez tecnica: compilacao estavel, contratos REST consistentes, HATEOAS, tratamento global de erros, documentacao completa e trilha de validacao automatizada/manual.

## 22. Informacoes a Complementar pelo Grupo
Preencher antes da submissao final:

### Integrantes
| Nome | RM | Papel principal no projeto |
|---|---|---|
| Integrante 1 | RMXXXXX | Backend e persistencia |
| Integrante 2 | RMXXXXX | API/Swagger e README |
| Integrante 3 | RMXXXXX | Testes e Postman |
| Integrante 4 | RMXXXXX | Arquitetura, cronograma e revisao final |

### Evidencias finais obrigatorias
- Link do video de apresentacao:
  - `https://SEU-LINK-DE-VIDEO`
- DER final:
  - `docs/imagens/der.png`
- Diagrama de classes final:
  - `docs/imagens/diagrama-classes.png`

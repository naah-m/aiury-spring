# Aiury API - Plataforma de Acolhimento

Backend Spring Boot para gerenciamento de usuarios, ajudantes, chats e mensagens no contexto de acolhimento emocional.

## 1. Problema Resolvido
O projeto resolve a falta de organizacao e rastreabilidade de atendimentos de acolhimento digital, estruturando dados de participantes, sessoes de conversa e historico de mensagens em uma API REST documentada.

## 2. Proposta da Solucao
A solucao implementa:
- cadastro e manutencao de usuarios e ajudantes;
- abertura, acompanhamento e encerramento de chats;
- registro de mensagens vinculadas aos chats;
- padronizacao de erros HTTP;
- navegacao HATEOAS nos recursos principais.

## 3. Publico-Alvo
- equipes academicas e tecnicas que precisam validar uma API REST completa;
- futuros consumidores frontend/mobile da plataforma Aiury;
- avaliadores de sprint com foco em modelagem, documentacao e qualidade tecnica.

## 4. Arquitetura da Aplicacao
A aplicacao segue arquitetura em camadas:
- `controller`: endpoints REST e contratos HTTP;
- `service`: regras de negocio e validacoes de existencia;
- `repository`: acesso a dados com Spring Data JPA;
- `entity`: modelagem relacional;
- `dto` e `mapper`: isolamento entre persistencia e contrato de API;
- `configs`: OpenAPI e tratamento global de excecoes.

Documento detalhado: `docs/arquitetura.md`

## 5. Tecnologias Utilizadas
- Java 21
- Spring Boot 3.5.6
- Spring Web
- Spring Data JPA
- Spring Validation
- Spring HATEOAS
- Springdoc OpenAPI (Swagger UI)
- Maven Wrapper
- Oracle JDBC (`ojdbc11`)
- H2 (testes automatizados)
- JUnit 5 / Mockito

## 6. Como Executar Localmente

### 6.1 Pre-requisitos
- JDK 21+
- Maven 3.9+ (opcional, pois o projeto usa `mvnw`)
- Banco Oracle acessivel (ambiente da disciplina ou local)

### 6.2 Configuracao de banco
O projeto usa variaveis de ambiente para evitar credenciais expostas:
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JPA_DDL_AUTO` (opcional, default `update`)
- `JPA_SHOW_SQL` (opcional, default `false`)

Exemplo PowerShell:

```powershell
$env:DB_URL="jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl"
$env:DB_USERNAME="SEU_USUARIO"
$env:DB_PASSWORD="SUA_SENHA"
```

### 6.3 Subir aplicacao

```powershell
.\mvnw.cmd spring-boot:run
```

## 7. Build e Testes

### Testes automatizados

```powershell
.\mvnw.cmd clean test
```

### Empacotamento

```powershell
.\mvnw.cmd clean package
```

Documentacao de testes: `docs/testes.md`

## 8. Swagger / OpenAPI
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- Atalho configurado: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## 9. Endpoints da API

| Recurso | Endpoint base | Operacoes |
|---|---|---|
| Usuarios | `/api/usuarios` | `GET`, `GET/{id}`, `POST`, `PUT/{id}`, `DELETE/{id}` |
| Ajudantes | `/api/ajudantes` | `GET`, `GET/{id}`, `POST`, `PUT/{id}`, `DELETE/{id}` |
| Chats | `/api/chats` | `GET`, `GET/{id}`, `POST`, `PUT/{id}`, `DELETE/{id}` |
| Mensagens | `/api/mensagens` | `GET`, `GET/{id}`, `POST`, `PUT/{id}`, `DELETE/{id}` |

Filtros implementados:
- `GET /api/usuarios?cidadeId=`
- `GET /api/ajudantes?disponivel=`
- `GET /api/chats?usuarioId=&ajudanteId=&status=`
- `GET /api/mensagens?chatId=&remetenteId=`

Detalhamento completo: `docs/endpoints.md`

## 10. Modelagem e Diagramas
Detalhes de entidades, cardinalidades e consistencia JPA:
- `docs/modelagem.md`

### DER (inserir imagem final do grupo)
Repositorio preparado para receber a imagem em:
- `docs/imagens/der.png`

### Diagrama de Classes (inserir imagem final do grupo)
Repositorio preparado para receber a imagem em:
- `docs/imagens/diagrama-classes.png`

## 11. Cronograma
Cronograma academico completo por atividade, responsavel e status:
- `docs/cronograma.md`

## 12. Postman e Evidencias
- Colecao: `docs/postman/Aiury-Sprint3.postman_collection.json`
- Environment: `docs/postman/Aiury-local.postman_environment.json`
- Guia de uso: `docs/postman/README.md`

## 13. Integrantes da Equipe

| Nome completo | Breve apresentacao | Responsabilidades no projeto |
|---|---|---|
| Preencher com integrante 1 | Perfil tecnico/acadêmico em 1 linha | Backend, modelagem JPA, qualidade de codigo |
| Preencher com integrante 2 | Perfil tecnico/acadêmico em 1 linha | Documentacao, Swagger/OpenAPI, README |
| Preencher com integrante 3 | Perfil tecnico/acadêmico em 1 linha | Testes, Postman, evidencias de validacao |
| Preencher com integrante 4 | Perfil tecnico/acadêmico em 1 linha | Arquitetura, cronograma, revisao final |

## 14. Video da Entrega
Link final da apresentacao (substituir antes da submissao):

`https://colocar-link-final-do-video`

## 15. Status da Entrega Sprint 3
Este repositorio foi organizado para entrega academica com:
- API REST padronizada com HATEOAS nos recursos principais;
- tratamento global de excecoes;
- testes automatizados minimos por camada;
- documentacao completa em `README` e pasta `docs`.

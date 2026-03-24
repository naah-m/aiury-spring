# Aiury

## 1) Nome da aplicacao
**Aiury** - API Java Spring Boot.

## 2) Visao resumida do problema e da solucao
O projeto trata o dominio de acolhimento entre pessoas usuarias e ajudantes, com foco em comunicacao estruturada e registro de interacoes.
A solucao atual implementa uma API REST com persistencia relacional para cadastro, consulta e manutencao das informacoes principais do dominio.

## 3) Escopo do MVP atual
O MVP atual esta concentrado no nucleo:
- `Usuario`
- `Ajudante`
- `Chat`
- `Mensagem`

Este recorte cobre o fluxo base de dados para evolucao incremental das proximas entregas.

## 4) Stack utilizada
- Java 21
- Spring Boot 3.5.6
- Spring Web
- Spring Data JPA
- Spring Validation
- Spring HATEOAS
- Lombok
- Oracle Database (driver `ojdbc11`)
- Maven (com Maven Wrapper)

## 5) Estrutura principal de pastas
```text
src/
  main/
    java/br/com/fiap/aiury/
      configs/
      controller/
      dto/
      entities/
      exceptions/
      mappers/
      repositories/
      services/
    resources/
      application.properties
  test/
    java/br/com/fiap/aiury/
```

## 6) Como rodar localmente
### Pre-requisitos
- JDK 21 instalado
- Maven (opcional, pois o projeto inclui `mvnw`)
- Acesso a uma instancia Oracle compativel com as configuracoes do projeto

### Passos
1. Clone o repositorio.
2. Ajuste as credenciais e URL do banco em `src/main/resources/application.properties`.
3. Execute a aplicacao:
   - Windows: `./mvnw spring-boot:run`
   - Linux/macOS: `./mvnw spring-boot:run`
4. A API sera iniciada na porta configurada (padrao atual: `8080`).

## 7) Secoes reservadas para entrega academica
### DER
`[PLACEHOLDER]` Inserir imagem/arquivo do DER final.

### Video de apresentacao
`[PLACEHOLDER]` Inserir link oficial do video.

### Documentacao de endpoints
- Arquivo consolidado: `docs/endpoints.md`
- Recursos documentados: `Usuario`, `Ajudante`, `Chat` e `Mensagem`

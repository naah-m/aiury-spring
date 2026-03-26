# Aiury - Sprint 3 (Oracle Only)

Aplicacao Spring Boot com frontend Thymeleaf para gestao de atendimentos entre usuario e ajudante, com persistencia relacional, migracoes versionadas e controle de acesso por perfil.

## 1. Stack da aplicacao
- Java 21
- Spring Boot 3.5.6
- Spring Web + Thymeleaf
- Spring Security
- Spring Data JPA + Hibernate
- Flyway
- Oracle Database (unico banco suportado)
- Springdoc OpenAPI
- JUnit 5 / Spring Test

## 2. Requisitos
- Java 21+
- Oracle Database acessivel (19c/21c/23c)
- Credenciais com permissao de criar/alterar objetos no schema da aplicacao

## 3. Configuracao
Arquivos principais:
- `src/main/resources/application.properties`
- `src/main/resources/application-oracle.properties`
- `src/main/resources/db/migration/oracle`

Variaveis obrigatorias:
- `DB_URL` (ex.: `jdbc:oracle:thin:@localhost:1521/FREEPDB1`)
- `DB_USERNAME`
- `DB_PASSWORD`

Variaveis opcionais:
- `SPRING_PROFILES_ACTIVE` (default: `oracle`)
- `JPA_DDL_AUTO` (default: `validate`)
- `JPA_SHOW_SQL` (default: `false`)

## 4. Subir a aplicacao

### Windows (PowerShell)
```powershell
$env:SPRING_PROFILES_ACTIVE="oracle"
$env:DB_URL="jdbc:oracle:thin:@localhost:1521/FREEPDB1"
$env:DB_USERNAME="AIURY_APP"
$env:DB_PASSWORD="SUA_SENHA"
.\mvnw.cmd clean spring-boot:run
```

### Linux/macOS
```bash
export SPRING_PROFILES_ACTIVE=oracle
export DB_URL=jdbc:oracle:thin:@localhost:1521/FREEPDB1
export DB_USERNAME=AIURY_APP
export DB_PASSWORD=SUA_SENHA
./mvnw clean spring-boot:run
```

Aplicacao local: `http://localhost:8080`

## 5. Flyway
O Flyway esta habilitado por padrao e usa somente scripts Oracle:
- `spring.flyway.locations=classpath:db/migration/oracle`
- migracoes em `src/main/resources/db/migration/oracle`
- estrutura: `V1` a `V5`
- dados iniciais controlados: `V6` a `V8`

## 6. Dados iniciais via Flyway
Os dados iniciais de demonstracao agora sobem automaticamente via SQL versionado no Flyway.
Nao existe mais seed Java por `ApplicationRunner` para dados de dominio.

Dados criados pelas migrations de seed:
- 2 estados
- 3 cidades
- 2 usuarios
- 2 ajudantes
- 2 chats
- 5 mensagens

## 7. Acesso web e API
- Home: `http://localhost:8080/`
- Login: `http://localhost:8080/login`
- Dashboard: `http://localhost:8080/app`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## 8. Credenciais
- Admin: `admin / admin123`
- Usuario demo: `11999998888 / demo12345`
- Ajudante demo: `ajudante.escuta / apoio12345`

## 9. Testes
### Suite padrao
```powershell
.\mvnw.cmd clean test
```
Executa testes unitarios/slice que nao exigem banco Oracle ativo.

### Integracao com Oracle (opcional)
Defina:
- `ORACLE_TEST_ENABLED=true`
- `TEST_DB_URL` (ou `DB_URL`)
- `TEST_DB_USERNAME` (ou `DB_USERNAME`)
- `TEST_DB_PASSWORD` (ou `DB_PASSWORD`)

Exemplo:
```powershell
$env:ORACLE_TEST_ENABLED="true"
$env:TEST_DB_URL="jdbc:oracle:thin:@localhost:1521/FREEPDB1"
$env:TEST_DB_USERNAME="AIURY_TEST"
$env:TEST_DB_PASSWORD="SUA_SENHA"
.\mvnw.cmd test
```

## 10. Estrutura relevante
```text
src/main/resources
|- application.properties
|- application-oracle.properties
|- db/migration/oracle
|- templates/
|- static/

src/test/resources
|- application-test.properties
```

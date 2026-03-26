# Estrategia de Testes e Evidencias

## 1. Objetivo
Comprovar que a entrega Sprint 3 esta:
- compilando sem quebra;
- com regras de negocio validadas;
- com fluxos web/API principais protegidos;
- coerente com ambiente Oracle-only.

## 2. Execucao automatizada oficial

Comandos obrigatorios:

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd clean package
```

Resultado esperado:
- `BUILD SUCCESS` nos dois comandos.

## 3. Suites de teste

### 3.1 Suite padrao (sempre executada)
- testes unitarios de service;
- testes de controller em `@WebMvcTest`.

Nao exige conexao ativa com Oracle.

### 3.2 Suite de integracao Oracle (condicional)
Classes de integracao e repository estao habilitadas apenas quando:
- `ORACLE_TEST_ENABLED=true`

E requerem:
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

## 4. Formatos de data validados
- `Usuario.dataNascimento`: `dd/MM/yyyy`
- `Chat.dataInicio` e `Chat.dataFim`: `dd/MM/yyyy HH:mm:ss`
- `Mensagem.dataEnvio`: `dd/MM/yyyy HH:mm:ss`

## 5. Ambiente de teste
- profile: `test`
- datasource: Oracle via variaveis de ambiente
- Flyway: `classpath:db/migration/oracle`
- dados iniciais: carregados por migrations SQL (`V6` a `V8`)

## 6. Evidencias para entrega
- log de `BUILD SUCCESS` em `clean test`;
- log de `BUILD SUCCESS` em `clean package`;
- evidencias de execucao da suite Oracle quando ambiente estiver disponivel.

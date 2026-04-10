# Estrategia de Testes e Evidencias

## 1. Objetivo
Garantir confiabilidade minima da entrega Sprint 3 em:
- build;
- regras de negocio;
- protecao de rotas;
- consistencia de contratos REST.

## 2. Execucao automatizada oficial
Comandos recomendados:

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd clean package
```

Resultado esperado:
- `BUILD SUCCESS` em ambos.

## 3. Suites de teste
### 3.1 Suite padrao (sem Oracle obrigatorio)
- testes unitarios de services;
- testes de controllers REST com `@WebMvcTest`;
- testes de handlers de seguranca e fluxos MVC isolados.

Locais no repositorio:
- `src/test/java/br/com/fiap/aiury/services`
- `src/test/java/br/com/fiap/aiury/controller`
- `src/test/java/br/com/fiap/aiury/controller/mvc`
- `src/test/java/br/com/fiap/aiury/security`
- `src/test/java/br/com/fiap/aiury/repositories`

### 3.2 Suite de integracao Oracle (condicional)
Ativacao por variavel:
- `ORACLE_TEST_ENABLED=true`

Variaveis de banco para integracao:
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

## 4. Roteiro rapido de validacao manual (banca)
1. Subir app com Oracle local e Flyway concluido.
2. Autenticar em `/login` com perfil ADMIN.
3. Validar acesso ao painel `/app`.
4. Validar operacao de usuarios e ajudantes.
5. Validar criacao/listagem de chat e conversa.
6. Validar que USUARIO e AJUDANTE nao acessam dados de terceiros.
7. Validar Swagger em `/swagger-ui.html`.

## 5. Cenarios criticos recomendados
- login com credencial invalida retorna erro amigavel;
- usuario sem permissao recebe `403`;
- endpoint protegido sem autenticacao retorna `401` (API) ou redireciona para login (MVC);
- mensagem em chat finalizado e bloqueada;
- tentativa de abrir segundo chat ativo para mesmo usuario e bloqueada.

## 6. Evidencias para entrega
- log de `BUILD SUCCESS`;
- captura do painel para cada perfil;
- captura de respostas `200/201/400/401/403/404/409` em cenarios relevantes;
- captura de Swagger listando recursos da API;
- captura de fluxo completo de abertura e acompanhamento de chat.

Collection para validacao de endpoints:
- `docs/postman/Aiury-Sprint3.postman_collection.json`
- `docs/postman/Aiury-local.postman_environment.json`
- `docs/postman/README.md`

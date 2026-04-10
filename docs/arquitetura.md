# Arquitetura da Aplicacao

## 1. Visao geral
O projeto segue arquitetura em camadas com separacao de responsabilidades para reduzir acoplamento e aumentar previsibilidade de manutencao.

## 2. Camadas e pacotes
| Camada | Pacotes principais | Responsabilidade |
|---|---|---|
| Web MVC | `controller.mvc`, `templates`, `fragments`, `static` | Fluxos de tela, formulários e navegação |
| API REST | `controller`, `representation`, `dto` | Endpoints, contratos e HATEOAS |
| Negocio | `services` | Regras de dominio e autorizacao contextual |
| Persistencia | `repositories`, `entities` | Mapeamento JPA e consultas |
| Conversao | `mappers`, `mappers.web` | Conversao entre entidades e DTOs/forms |
| Infra | `configs`, `security`, `exceptions` | Config global, seguranca e tratamento de erro |

## 3. Fluxo tecnico de requisicao
1. Requisicao entra via controller MVC ou REST.
2. Validacoes de entrada (Bean Validation + checks de negocio).
3. Service aplica regras e autorizacao por contexto.
4. Repository executa consulta/escrita no Oracle.
5. Mapper converte resultado para DTO/view.
6. Controller retorna resposta HTML ou JSON.

## 4. Seguranca em profundidade
- Regras de rota definidas em `SecurityConfig`.
- Regras de vinculo reforcadas na camada de servico:
  - usuario e ajudante so acessam seus chats/mensagens;
  - operacoes administrativas restritas a `ADMIN`;
  - controle de envio de mensagem por remetente valido.
- Login centralizado com `AiuryUserDetailsService`, resolvendo credencial em 3 fontes:
  - `TB_ADMIN_ACCOUNT` (admin),
  - `TB_USUARIO` (celular),
  - `TB_AJUDANTE` (login).

## 5. Decisoes de arquitetura da Sprint 3
- DTO separado para request/response em recursos REST.
- DTO/web forms separados para MVC.
- HATEOAS aplicado em todos os recursos principais da API.
- Tratamento global de excecoes com payload padronizado (`ApiErrorResponse`).
- Flyway Oracle-only como mecanismo oficial de evolucao de schema e seed.

## 6. Qualidade e operacao
- Build e testes automatizados via Maven Wrapper.
- Suite de integracao Oracle condicional por ambiente.
- Diagrama de arquitetura versionado em `docs/diagramas/arquitetura.mmd` e exportado em `docs/imagens/arquitetura.png`.
- Documentacao tecnica complementar em:
  - `docs/endpoints.md`
  - `docs/modelagem.md`
  - `docs/testes.md`
  - `docs/visao-mvp.md`

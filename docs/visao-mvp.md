# Visao do MVP - Sprint 3

## 1. Problema atendido
Atendimentos sem rastreabilidade e sem controle de acesso por perfil geram perda de contexto, risco operacional e baixa capacidade de auditoria.

## 2. Solucao entregue no MVP
O Aiury entrega uma base funcional com:
- autenticacao por login;
- autorizacao por perfil (`ADMIN`, `USUARIO`, `AJUDANTE`);
- gestao de usuarios, ajudantes, chats e mensagens;
- interface web MVC para operacao diaria;
- API REST com HATEOAS para integracao e validacao tecnica.

## 3. Escopo implementado
- CRUD completo de `Estado`, `Cidade`, `Usuario`, `Ajudante`, `Chat` e `Mensagem`;
- fluxo web para abertura, acompanhamento e conversa de chats;
- regra de acesso por vinculo (usuario/ajudante so acessa o proprio contexto);
- regra de negocio para impedir mais de um chat ativo por usuario;
- Flyway Oracle-only com migrations versionadas e seed local;
- Swagger/OpenAPI com exemplos de payload e erros;
- testes automatizados de service e controllers (além de integracao Oracle condicional).

## 4. Escopo fora desta entrega
- notificacoes em tempo real (WebSocket);
- integracoes externas de mensageria;
- observabilidade avancada (tracing distribuido, dashboards operacionais);
- pipeline CI/CD completo com gates de qualidade.

## 5. Resultado esperado para avaliacao
Entrega com:
- fluxo principal funcionando em ambiente local Oracle;
- seguranca aplicada no backend e na camada web;
- documentacao tecnica alinhada ao codigo;
- base pronta para evolucao de features sem retrabalho estrutural.

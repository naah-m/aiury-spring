# Visao do MVP

## Problema
Atendimentos emocionais sem estrutura tecnica dificultam continuidade, controle e melhoria do processo.

## Solucao no MVP
API REST para registrar:
- catalogos (`Estado`, `Cidade`);
- usuarios e ajudantes;
- chats de acolhimento;
- mensagens por chat.

## Escopo entregue
- CRUD completo dos recursos.
- Regras de negocio basicas de consistencia temporal e referencial.
- HATEOAS para navegacao entre recursos.
- Swagger/OpenAPI e documentacao markdown.
- Testes automatizados e roteiro Postman.

## Fora do escopo desta entrega
- Autenticacao e autorizacao.
- Integracao em tempo real (WebSocket).
- Telemetria e observabilidade avancada.

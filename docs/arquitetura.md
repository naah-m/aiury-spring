# Arquitetura da Aplicacao

## Visao Geral
O projeto segue arquitetura em camadas para separar responsabilidades e facilitar manutencao.

## Camadas

| Camada | Pacote | Responsabilidade |
|---|---|---|
| Entrada HTTP | `controller` | Expor endpoints REST, validar payload e retornar status HTTP corretos |
| Representacao | `dto`, `controller/*Assembler` | Definir contratos de entrada/saida e links HATEOAS |
| Negocio | `services` | Regras de negocio, validacoes e orquestracao de operacoes |
| Persistencia | `repositories` | Consultas e escrita em banco com Spring Data JPA |
| Dominio | `entities`, `entities/ChatStatus` | Modelo relacional do negocio |
| Infra/API | `configs`, `exceptions` | OpenAPI, tratamento global de erros e padronizacao de respostas |

## Fluxo de Requisicao
1. Controller recebe a requisicao e valida o DTO.
2. Service aplica regras de negocio e valida existencia de referencias.
3. Repository executa persistencia/consulta.
4. Service devolve entidade de dominio.
5. Assembler converte para representacao HATEOAS.
6. Controller retorna resposta HTTP com status adequado.

## Padroes Adotados
- DTO + Mapper para desacoplamento entre API e entidade.
- HATEOAS para navegacao entre recursos.
- Exception Handler global para padronizar erros.
- Perfis de ambiente para execucao local e testes.

## Diagrama Conceitual (texto)
`Controller -> Service -> Repository -> Banco`

`Controller -> Assembler -> DTO + Links HATEOAS`

## Decisoes Tecnicas Relevantes
- `UsuarioResponseDTO` nao expoe senha.
- Queries em repository utilizam apenas campos existentes nas entidades.
- Filtros opcionais em endpoints `GET all` para reduzir acoplamento de consultas.
- Testes automatizados usam H2 em memoria para estabilidade de build.

# Arquitetura da Aplicacao - Aiury API

## 1. Visao Arquitetural
A aplicacao adota arquitetura em camadas com separacao objetiva de responsabilidades, reduzindo acoplamento e facilitando manutencao, testes e evolucao incremental da API.

## 2. Camadas e Responsabilidades
| Camada | Pacote | Responsabilidade |
|---|---|---|
| Entrada HTTP | `controller` | Expor endpoints REST, receber `RequestDTO`, validar dados e retornar status HTTP |
| Contratos | `dto` | Definir modelos de request/response e padrao de erro |
| Representacao | `representation` | Construir respostas HATEOAS (`self`, navegacao, relacoes) |
| Negocio | `services` | Aplicar regras, validar existencia de referencias e orquestrar fluxos |
| Conversao | `mappers` | Converter entidade <-> DTO de forma consistente |
| Persistencia | `repositories` | Operacoes JPA, queries derivadas e filtros |
| Dominio | `entities` | Modelagem relacional e mapeamentos JPA |
| Infraestrutura | `configs`, `exceptions` | OpenAPI, configuracoes e tratamento global de excecoes |

## 3. Fluxo de Requisicao
1. `Controller` recebe requisição e valida `RequestDTO` com Bean Validation.
2. `Service` executa regras de negocio e valida integridade referencial.
3. `Repository` persiste/consulta dados.
4. `Mapper` converte entidade para `ResponseDTO`.
5. `RepresentationBuilder` adiciona links HATEOAS.
6. `Controller` devolve resposta HTTP padronizada.

## 4. Decisoes Tecnicas Relevantes
- Padrao `RequestDTO` e `ResponseDTO` em todos os recursos principais.
- Controllers sem `ModelAssembler`, mantendo a camada HTTP enxuta.
- HATEOAS implementado por builders dedicados em pacote separado.
- `GlobalExceptionHandler` com payload unico de erro (`ApiErrorResponse`).
- Repositories com query methods validados por testes de persistencia.

## 5. Qualidade e Confiabilidade
### Garantias aplicadas no codigo
- Build validado com `mvn clean test` e `mvn clean package`.
- Testes unitarios, web e JPA cobrindo fluxo principal e queries derivadas.
- OpenAPI com metadados, descricoes e codigos de resposta.

### Riscos mitigados
- Erros de query JPA por campo inexistente (causa da penalidade Sprint 2) cobertos por testes de repository.
- Variações de payload invalido tratadas como `400` com detalhe por campo.
- Recursos inexistentes tratados como `404` de forma padronizada.

## 6. Organizacao de Pacotes
```text
br.com.fiap.aiury
├─ controller
├─ services
├─ repositories
├─ entities
├─ dto
├─ mappers
├─ representation
├─ configs
└─ exceptions
```

## 7. Aderencia a REST Nivel 3
- Respostas dos recursos principais incluem links de navegacao.
- Links contemplam `self`, colecao, atualizacao, exclusao e relacionamentos.
- A API permite navegacao orientada a hipermidia sem acoplamento forte ao cliente.

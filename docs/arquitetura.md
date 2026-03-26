# Arquitetura da Aplicacao

## 1. Visão geral
A API segue arquitetura em camadas com separação de responsabilidade para reduzir acoplamento e facilitar manutenção.

## 2. Camadas

| Camada | Pacote | Papel |
|---|---|---|
| HTTP | `controller` | Endpoints REST, status code e contrato de entrada/saida |
| Regras | `services` | Validações de negocio e orquestracao de fluxo |
| Persistencia | `repositories` | Acesso ao banco com Spring Data JPA |
| Dominio | `entities` | Modelagem relacional com JPA |
| Contratos | `dto` | Payloads de request e response |
| Conversao | `mappers` | Conversao entre entidade e DTO |
| Hipermidia | `representation` | Montagem de links HATEOAS |
| Infra | `configs`, `exceptions` | Swagger/OpenAPI e tratamento de erros |

## 3. Fluxo de requisição
1. Controller recebe request e aplica validação Bean Validation.
2. Service executa regras e valida referencias.
3. Repository consulta/persiste.
4. Mapper converte entidade para DTO.
5. Representation builder adiciona links HATEOAS.
6. Controller retorna resposta HTTP.

## 4. Decisoes tecnicas
- DTO separado para request e response em todos os recursos.
- HATEOAS aplicado em `Estados`, `Cidades`, `Usuarios`, `Ajudantes`, `Chats`, `Mensagens`.
- Tratamento global de excecoes com payload unico `ApiErrorResponse`.
- Validações de conflito (`409`) para unicidade e integridade.
- Perfil `dev` com H2 por padrao e perfil `oracle` para ambiente real.

## 5. Garantias de qualidade
- Build validado com `mvn clean test` e `mvn clean package`.
- Testes em camadas de controller, service e repository.
- Documentacao OpenAPI e docs markdown alinhadas ao codigo.


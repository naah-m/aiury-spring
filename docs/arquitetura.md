# Arquitetura da Aplicacao

## 1. Visao geral
A API segue arquitetura em camadas com separacao de responsabilidade para reduzir acoplamento e facilitar manutencao.

## 2. Camadas

| Camada | Pacote | Papel |
|---|---|---|
| HTTP | `controller` | Endpoints REST e MVC, status code e contratos de entrada/saida |
| Regras | `services` | Validacoes de negocio e orquestracao de fluxo |
| Persistencia | `repositories` | Acesso ao banco com Spring Data JPA |
| Dominio | `entities` | Modelagem relacional com JPA |
| Contratos | `dto` | Payloads de request e response |
| Conversao | `mappers` | Conversao entre entidade e DTO |
| Hipermidia | `representation` | Montagem de links HATEOAS |
| Infra | `configs`, `exceptions`, `security` | Configuracoes globais, tratamento de erros e seguranca |

## 3. Fluxo de requisicao
1. Controller recebe request e aplica Bean Validation.
2. Service executa regras de negocio.
3. Repository consulta/persiste no Oracle.
4. Mapper converte entidade para DTO.
5. Representation builder adiciona links HATEOAS.
6. Controller retorna resposta HTTP.

## 4. Decisoes tecnicas
- DTO separado para request e response em todos os recursos.
- HATEOAS aplicado em `Estados`, `Cidades`, `Usuarios`, `Ajudantes`, `Chats` e `Mensagens`.
- Tratamento global de excecoes com payload unico `ApiErrorResponse`.
- Validacoes de conflito (`409`) para unicidade e integridade.
- Banco padrao e unico da aplicacao: Oracle.
- Flyway configurado somente para `db/migration/oracle`.

## 5. Garantias de qualidade
- Build validado com `mvn clean test`.
- Testes unitarios, web slices e integracao Oracle condicional por ambiente.
- Documentacao OpenAPI e markdown alinhadas ao codigo.


# Modelagem de Dados - Aiury

## Objetivo
Documentar o modelo relacional e sua correspondencia com as classes JPA para garantir consistencia entre DER, diagrama de classes e implementacao.

## Entidades e Tabelas

| Entidade JPA | Tabela | Chave primaria | Campos principais |
|---|---|---|---|
| `Usuario` | `usuario` | `id` | `nome_real`, `nome_anonimo`, `data_nascimento`, `celular`, `senha`, `data_cadastro`, `id_cidade` |
| `Ajudante` | `tb_ajudante` | `id` | `area_atuacao`, `motivacao`, `disponivel`, `rating` |
| `Chat` | `chat` | `id` | `id_usuario`, `id_ajudante`, `data_inicio`, `data_fim`, `status` |
| `Mensagem` | `tb_mensagem` | `id_mensagem` | `id_chat`, `id_remetente`, `texto`, `data_envio` |
| `Cidade` | `cidade` | `id` | `nome_cidade`, `id_estado` |
| `Estado` | `estado` | `id` | `nome_estado`, `uf` |

## Relacionamentos Principais

| Relacionamento | Cardinalidade | FK |
|---|---|---|
| `Estado` -> `Cidade` | 1:N | `cidade.id_estado` |
| `Cidade` -> `Usuario` | 1:N | `usuario.id_cidade` |
| `Usuario` -> `Chat` | 1:N | `chat.id_usuario` |
| `Ajudante` -> `Chat` | 1:N | `chat.id_ajudante` |
| `Chat` -> `Mensagem` | 1:N | `tb_mensagem.id_chat` |
| `Usuario` -> `Mensagem` (remetente) | 1:N | `tb_mensagem.id_remetente` |

## Consistencia com a API
- DTOs de entrada (`UsuarioDTO`, `AjudanteDTO`, `ChatDTO`, `MensagemDTO`) refletem os campos obrigatorios de criacao/atualizacao.
- DTO de resposta de usuario (`UsuarioResponseDTO`) nao expoe `senha`.
- Mappers garantem conversao coerente entre contrato HTTP e modelo JPA.
- Repositories usam campos existentes nas entidades, evitando erros de compilacao por query method invalido.

## Observacoes de Constraints
- `usuario.celular` possui restricao de unicidade.
- `usuario.id_cidade`, `chat.id_usuario`, `chat.id_ajudante`, `tb_mensagem.id_chat` e `tb_mensagem.id_remetente` sao obrigatorios.
- `chat.status` usa enum `ChatStatus` persistido como `STRING`.

## DER e Diagrama de Classes

### DER
Inserir imagem final em:
- `docs/imagens/der.png`

### Diagrama de Classes
Inserir imagem final em:
- `docs/imagens/diagrama-classes.png`

## Checklist de Coerencia para Entrega
- DER representa as mesmas cardinalidades definidas nas entidades JPA.
- Chaves estrangeiras no DER correspondem aos `@JoinColumn` do codigo.
- Diagrama de classes inclui `ChatStatus` como enumeracao vinculada a `Chat`.
- Nomenclaturas de atributos no DER e no codigo seguem o mesmo significado funcional.

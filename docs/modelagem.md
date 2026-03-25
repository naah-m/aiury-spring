# Modelagem de Dados e Consistencia JPA

## 1. Objetivo
Documentar o modelo de dados da API Aiury e comprovar coerencia entre:
- entidade JPA;
- estrutura relacional;
- repositories;
- contratos de API.

## 2. Entidades e Tabelas
| Entidade | Tabela | PK | Campos-chave |
|---|---|---|---|
| `Usuario` | `usuario` | `id` | `nome_real`, `nome_anonimo`, `data_nascimento`, `celular`, `senha`, `data_cadastro`, `id_cidade` |
| `Ajudante` | `tb_ajudante` | `id` | `area_atuacao`, `motivacao`, `disponivel`, `rating` |
| `Chat` | `chat` | `id` | `id_usuario`, `id_ajudante`, `data_inicio`, `data_fim`, `status` |
| `Mensagem` | `tb_mensagem` | `id_mensagem` | `id_chat`, `id_remetente`, `texto`, `data_envio` |
| `Cidade` | `cidade` | `id` | `nome_cidade`, `id_estado` |
| `Estado` | `estado` | `id` | `nome_estado`, `uf` |

## 3. Relacionamentos e Cardinalidades
| Relacionamento | Cardinalidade | Implementacao JPA | FK |
|---|---|---|---|
| `Estado -> Cidade` | 1:N | `Cidade @ManyToOne Estado` | `cidade.id_estado` |
| `Cidade -> Usuario` | 1:N | `Usuario @ManyToOne Cidade` | `usuario.id_cidade` |
| `Usuario -> Chat` | 1:N | `Chat @ManyToOne Usuario` | `chat.id_usuario` |
| `Ajudante -> Chat` | 1:N | `Chat @ManyToOne Ajudante` | `chat.id_ajudante` |
| `Chat -> Mensagem` | 1:N | `Chat @OneToMany Mensagem` | `tb_mensagem.id_chat` |
| `Usuario -> Mensagem` | 1:N | `Mensagem @ManyToOne Usuario` | `tb_mensagem.id_remetente` |

## 4. Regras de Integridade Aplicadas
- `usuario.celular` com unicidade.
- FKs obrigatorias em `usuario`, `chat` e `tb_mensagem`.
- Enum de status de chat persistido como `STRING` (`INICIADO`, `EM_ANDAMENTO`, `FINALIZADO_USUARIO`, `FINALIZADO_AJUDANTE`, `FINALIZADO_SISTEMA`).
- `Chat` com `cascade = ALL` e `orphanRemoval = true` para mensagens associadas.

## 5. Coerencia com Repositories
Queries derivadas em uso:
- `UsuarioRepository.findByCidade_Id(...)`
- `AjudanteRepository.findByDisponivel(...)`
- `MensagemRepository.findByChat_IdOrderByDataEnvioAsc(...)`
- `MensagemRepository.findByRemetente_IdOrderByDataEnvioAsc(...)`
- `MensagemRepository.findByChat_IdAndRemetente_IdOrderByDataEnvioAsc(...)`

Todas referenciam campos existentes nas entidades e sao cobertas por testes automatizados.

## 6. Coerencia com API (Request/Response)
- Cada recurso principal usa DTOs separados de request e response.
- Campos sensiveis (`senha`) nao sao expostos em respostas.
- IDs de relacionamento sao recebidos por request e resolvidos na camada de servico.

## 7. DER e Diagrama de Classes
### DER
- Arquivo esperado: `docs/imagens/der.png`

### Diagrama de Classes
- Arquivo esperado: `docs/imagens/diagrama-classes.png`

## 8. Checklist de Consistencia Final
- Relacionamentos do DER batem com `@JoinColumn`.
- Chaves e constraints batem com anotações JPA.
- Repositories nao usam campos inexistentes.
- Contratos HTTP refletem o modelo relacional sem expor internals indevidos.

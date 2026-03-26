# Modelagem de Dominio e Persistencia

## 1. Objetivo deste documento
Registrar a modelagem final da Sprint 3, com foco em coerencia entre:
- entidades JPA;
- constraints no banco;
- contratos DTO;
- repositories e regras de servico.

## 2. Entidades do dominio

| Entidade | Responsabilidade | Tabela |
|---|---|---|
| `Estado` | Catalogo de UFs | `estado` |
| `Cidade` | Catalogo de cidades por estado | `cidade` |
| `Usuario` | Pessoa usuaria da plataforma | `usuario` |
| `Ajudante` | Perfil de apoio no atendimento | `tb_ajudante` |
| `Chat` | Sessao de atendimento entre usuario e ajudante | `chat` |
| `Mensagem` | Registro textual do chat | `tb_mensagem` |

## 3. Relacionamentos e cardinalidade

| Origem | Destino | Cardinalidade | Implementacao |
|---|---|---|---|
| Estado | Cidade | 1:N | `Estado @OneToMany` / `Cidade @ManyToOne` |
| Cidade | Usuario | 1:N | `Usuario @ManyToOne` |
| Usuario | Chat | 1:N | `Chat @ManyToOne` |
| Ajudante | Chat | 1:N | `Chat @ManyToOne` |
| Chat | Mensagem | 1:N | `Chat @OneToMany` com `orphanRemoval=true` |
| Usuario | Mensagem | 1:N | `Mensagem @ManyToOne` (`remetente`) |

## 4. Constraints aplicadas

- `estado.uf` unico.
- `estado.nome_estado` unico.
- `cidade` com unicidade composta (`nome_cidade`, `id_estado`).
- `usuario.celular` unico.
- FKs obrigatorias em `cidade`, `usuario`, `chat` e `mensagem`.
- `chat.status` persistido como enum string.

## 5. Coerencia entre modelagem e repositories

Query methods ativos e validados:
- `EstadoRepository.findByUfIgnoreCase(...)`
- `CidadeRepository.findByEstado_IdOrderByNomeCidadeAsc(...)`
- `UsuarioRepository.findByCidade_Id(...)`
- `AjudanteRepository.findByDisponivel(...)`
- `MensagemRepository.findByChat_IdOrderByDataEnvioAsc(...)`
- `MensagemRepository.findByChat_IdAndRemetente_IdOrderByDataEnvioAsc(...)`

Todos os campos usados nos metodos derivados existem nas entidades.

## 6. Decisao sobre Cidade e Estado

### 6.1 Faz sentido manter Cidade e Estado no dominio?
Sim. Elas deixaram de ser entidades soltas e passaram a operar como catalogo oficial para validação geografica de usuarios.

### 6.2 Elas agregam valor real?
Sim. Agora sustentam:
- integridade referencial de `Usuario`;
- filtros de busca (`/api/cidades?estadoId=...`, `/api/usuarios?cidadeId=...`);
- navegação HATEOAS entre usuario, cidade e estado.

### 6.3 O relacionamento Cidade -> Estado esta correto?
Sim. Foi mantido como `@ManyToOne` com FK obrigatória e unicidade composta para evitar duplicidade de cidade no mesmo estado.

### 6.4 Usuario deve apontar para Cidade e Estado vir por consequencia?
Sim. A referencia de `Usuario` fica em `Cidade`; `Estado` e derivado via relacionamento da cidade.

### 6.5 Vale expor endpoint para Cidade e Estado?
Sim. Foram adicionados endpoints CRUD para ambos os catalogos, evitando dependencia de insercao manual no banco.

### 6.6 Qual foi a ação aplicada na Sprint 3?
Manter e fortalecer. O modelo não foi removido; foi integrado por completo em DTO, service, controller, HATEOAS, repositorio e documentacao.

## 7. Regras de negocio adicionadas

- Chat finalizado exige `dataFim`.
- Chat `INICIADO` não aceita `dataFim`.
- Mensagem deve respeitar janela temporal do chat.
- Remetente da mensagem deve pertencer ao chat.
- Cidade/Estado e celular de usuario possuem validação de unicidade com resposta `409`.

## 8. DER e diagrama de classes

- DER final: `docs/imagens/der.png`
- Diagrama de classes final: `docs/imagens/diagrama-classes.png`

Esses artefatos devem refletir exatamente as entidades acima e seus relacionamentos.


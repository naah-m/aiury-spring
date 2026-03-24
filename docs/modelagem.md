# Modelagem de Dados (Base para DER e MER)

## Objetivo
Este documento descreve o modelo de dados atual do projeto para apoiar a construcao do DER (Diagrama Entidade-Relacionamento), do MER e do diagrama de classes.

## Entidades atuais

### Usuario
- **Chave primaria:** `id`
- **Atributos principais:** `nomeReal`, `nomeAnonimo`, `dataNascimento`, `celular`, `senha`, `dataCadastro`
- **Relacionamento direto:** `cidade` (`ManyToOne` com `Cidade`)

### Ajudante
- **Chave primaria:** `id`
- **Atributos principais:** `areaAtuacao`, `motivacao`, `disponivel`, `rating`
- **Relacionamento direto:** referenciado por `Chat` (lado inverso nao mapeado na classe)

### Chat
- **Chave primaria:** `id`
- **Atributos principais:** `dataInicio`, `dataFim`, `status`
- **Relacionamentos diretos:**
- `usuario` (`ManyToOne` com `Usuario`, obrigatorio no mapeamento atual)
- `ajudante` (`ManyToOne` com `Ajudante`, opcional no mapeamento atual)
- `mensagens` (`OneToMany` com `Mensagem`)

### Mensagem
- **Chave primaria:** `id` (`id_mensagem`)
- **Atributos principais:** `texto`, `dataEnvio`
- **Relacionamentos diretos:**
- `chat` (`ManyToOne` com `Chat`, obrigatorio)
- `remetente` (`ManyToOne` com `Usuario`, obrigatorio)

### Cidade
- **Chave primaria:** `id`
- **Atributos principais:** `nomeCidade`
- **Relacionamento direto:** `estado` (`ManyToOne` com `Estado`)

### Estado
- **Chave primaria:** `id`
- **Atributos principais:** `nomeEstado`, `uf`
- **Relacionamento direto:** referenciado por `Cidade` (lado inverso nao mapeado na classe)

## Relacionamentos e cardinalidades
- `Estado (1) -> (N) Cidade`
- `Cidade (1) -> (N) Usuario`
- `Usuario (1) -> (N) Chat`
- `Ajudante (1) -> (N) Chat`, com opcionalidade de `Ajudante` no lado de `Chat` (`0..1`)
- `Chat (1) -> (N) Mensagem`
- `Usuario (1) -> (N) Mensagem` (como remetente)

## Como representar no DER/MER
- Criar uma entidade para cada classe: `Usuario`, `Ajudante`, `Chat`, `Mensagem`, `Cidade`, `Estado`.
- Marcar as chaves primarias em cada entidade (`id` ou `id_mensagem` em `Mensagem`).
- Representar chaves estrangeiras no lado N:
- `Usuario` referencia `Cidade`.
- `Cidade` referencia `Estado`.
- `Chat` referencia `Usuario` e `Ajudante`.
- `Mensagem` referencia `Chat` e `Usuario` (remetente).
- Destacar obrigatoriedade conforme mapeamento atual:
- `Chat.usuario` obrigatorio.
- `Chat.ajudante` opcional.
- `Mensagem.chat` obrigatorio.
- `Mensagem.remetente` obrigatorio.
- No diagrama de classes, manter `ChatStatus` como enumeracao associada a `Chat`.

## Observacao de escopo
Esta documentacao descreve o **MVP atual**, centrado no nucleo `Usuario/Ajudante/Chat/Mensagem` com suporte de localizacao (`Cidade/Estado`).

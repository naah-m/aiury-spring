# Modelagem de Dominio e Persistencia

## 1. Convencao corporativa adotada

### 1.1 Tabelas
- Prefixo obrigatorio: `TB_`
- Exemplo: `TB_USUARIO`, `TB_CHAT`, `TB_MENSAGEM`

### 1.2 Colunas
- `ID_`: identificador
- `NM_`: nome
- `DS_`: descricao
- `TX_`: texto
- `ST_`: status
- `DH_`: data/hora
- `DT_`: data
- `NR_`: numero
- `FL_`: flag booleana
- `SG_`: sigla

### 1.3 Constraints e indexes
- `PK_` para chave primaria
- `FK_` para chave estrangeira
- `UK_` para unicidade
- `CK_` para check
- `IX_` para index auxiliar

## 2. Tabelas e colunas finais

### TB_ESTADO
- `ID_ESTADO`
- `NM_ESTADO`
- `SG_ESTADO`

### TB_CIDADE
- `ID_CIDADE`
- `NM_CIDADE`
- `ID_ESTADO`

### TB_USUARIO
- `ID_USUARIO`
- `NM_USUARIO_REAL`
- `NM_USUARIO_ANONIMO`
- `DT_NASCIMENTO`
- `NR_CELULAR`
- `DS_SENHA`
- `DT_CADASTRO`
- `ID_CIDADE`

### TB_AJUDANTE
- `ID_AJUDANTE`
- `NM_AREA_ATUACAO`
- `NM_LOGIN`
- `DS_SENHA`
- `DS_MOTIVACAO`
- `FL_DISPONIVEL`
- `NR_RATING`

### TB_CHAT
- `ID_CHAT`
- `ID_USUARIO`
- `ID_AJUDANTE`
- `DH_INICIO`
- `DH_FIM`
- `ST_CHAT`

### TB_MENSAGEM
- `ID_MENSAGEM`
- `ID_CHAT`
- `ID_USUARIO_REMETENTE`
- `ID_AJUDANTE_REMETENTE`
- `TX_MENSAGEM`
- `DH_ENVIO`

### TB_ADMIN_ACCOUNT
- `ID_ADMIN_ACCOUNT`
- `NM_LOGIN`
- `DS_SENHA`
- `DH_ATUALIZACAO`

## 3. Relacionamentos
- `TB_CIDADE.ID_ESTADO -> TB_ESTADO.ID_ESTADO`
- `TB_USUARIO.ID_CIDADE -> TB_CIDADE.ID_CIDADE`
- `TB_CHAT.ID_USUARIO -> TB_USUARIO.ID_USUARIO`
- `TB_CHAT.ID_AJUDANTE -> TB_AJUDANTE.ID_AJUDANTE`
- `TB_MENSAGEM.ID_CHAT -> TB_CHAT.ID_CHAT`
- `TB_MENSAGEM.ID_USUARIO_REMETENTE -> TB_USUARIO.ID_USUARIO`
- `TB_MENSAGEM.ID_AJUDANTE_REMETENTE -> TB_AJUDANTE.ID_AJUDANTE`

## 4. Observacoes de integridade
- `ST_CHAT` permanece `EnumType.STRING` com os mesmos valores de dominio.
- Mensagem continua com remetente exclusivo (usuario ou ajudante), validado por check constraint.
- Todas as mudancas de nomenclatura foram versionadas no Flyway em `V5__standardize_corporate_naming.sql`.


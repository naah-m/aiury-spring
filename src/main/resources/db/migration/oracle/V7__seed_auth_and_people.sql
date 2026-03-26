-- Seed de autenticacao e pessoas base para demonstracao.
-- Senhas em BCrypt para compatibilidade direta com Spring Security.

INSERT INTO TB_ADMIN_ACCOUNT (NM_LOGIN, DS_SENHA, DH_ATUALIZACAO)
SELECT 'admin', '$2a$10$eBBKEef4FcRo2B.BwlH3b.k1fLV0MGci/cs.rFn2isCYA7cPa5QU2', SYSTIMESTAMP
FROM dual
WHERE NOT EXISTS (
    SELECT 1
    FROM TB_ADMIN_ACCOUNT a
    WHERE UPPER(a.NM_LOGIN) = UPPER('admin')
);

INSERT INTO TB_USUARIO (
    NM_USUARIO_REAL,
    NM_USUARIO_ANONIMO,
    DT_NASCIMENTO,
    NR_CELULAR,
    DS_SENHA,
    DT_CADASTRO,
    ID_CIDADE
)
SELECT
    'Camila Nunes',
    'AuroraCalma',
    DATE '1998-08-15',
    '11999998888',
    '$2a$10$a3/.1F5QGd2ytXuKdAJwh.OdKTxdNEKzb4wU63XE.CvoWO1TvLxxu',
    DATE '2026-03-25',
    c.ID_CIDADE
FROM TB_CIDADE c
JOIN TB_ESTADO e ON e.ID_ESTADO = c.ID_ESTADO
WHERE c.NM_CIDADE = 'Sao Paulo'
  AND e.SG_ESTADO = 'SP'
  AND NOT EXISTS (
    SELECT 1
    FROM TB_USUARIO u
    WHERE u.NR_CELULAR = '11999998888'
);

INSERT INTO TB_USUARIO (
    NM_USUARIO_REAL,
    NM_USUARIO_ANONIMO,
    DT_NASCIMENTO,
    NR_CELULAR,
    DS_SENHA,
    DT_CADASTRO,
    ID_CIDADE
)
SELECT
    'Diego Pereira',
    'RitmoSereno',
    DATE '1994-02-11',
    '11988887777',
    '$2a$10$a3/.1F5QGd2ytXuKdAJwh.OdKTxdNEKzb4wU63XE.CvoWO1TvLxxu',
    DATE '2026-03-25',
    c.ID_CIDADE
FROM TB_CIDADE c
JOIN TB_ESTADO e ON e.ID_ESTADO = c.ID_ESTADO
WHERE c.NM_CIDADE = 'Campinas'
  AND e.SG_ESTADO = 'SP'
  AND NOT EXISTS (
    SELECT 1
    FROM TB_USUARIO u
    WHERE u.NR_CELULAR = '11988887777'
);

INSERT INTO TB_AJUDANTE (
    NM_AREA_ATUACAO,
    NM_LOGIN,
    DS_SENHA,
    DS_MOTIVACAO,
    FL_DISPONIVEL,
    NR_RATING
)
SELECT
    'Escuta ativa',
    'ajudante.escuta',
    '$2a$10$Ed9EVPl00zT5e1oSEUmUq.mPwCjxKhIC4kfO8dSjdBPC7/Xz4Sfeq',
    'Acolhimento inicial e escuta qualificada para momentos de vulnerabilidade.',
    1,
    4.9
FROM dual
WHERE NOT EXISTS (
    SELECT 1
    FROM TB_AJUDANTE a
    WHERE UPPER(a.NM_LOGIN) = UPPER('ajudante.escuta')
);

INSERT INTO TB_AJUDANTE (
    NM_AREA_ATUACAO,
    NM_LOGIN,
    DS_SENHA,
    DS_MOTIVACAO,
    FL_DISPONIVEL,
    NR_RATING
)
SELECT
    'Plantao emocional',
    'ajudante.plantao',
    '$2a$10$Ed9EVPl00zT5e1oSEUmUq.mPwCjxKhIC4kfO8dSjdBPC7/Xz4Sfeq',
    'Atendimento em janelas agendadas para suporte pontual.',
    0,
    4.6
FROM dual
WHERE NOT EXISTS (
    SELECT 1
    FROM TB_AJUDANTE a
    WHERE UPPER(a.NM_LOGIN) = UPPER('ajudante.plantao')
);

-- Normaliza credenciais legadas em texto puro para BCrypt.
-- Objetivo: impedir dependencia de fallback inseguro no PasswordEncoder.

UPDATE TB_ADMIN_ACCOUNT
SET DS_SENHA = '$2a$10$eBBKEef4FcRo2B.BwlH3b.k1fLV0MGci/cs.rFn2isCYA7cPa5QU2'
WHERE TRIM(DS_SENHA) = 'admin123';

UPDATE TB_USUARIO
SET DS_SENHA = '$2a$10$a3/.1F5QGd2ytXuKdAJwh.OdKTxdNEKzb4wU63XE.CvoWO1TvLxxu'
WHERE TRIM(DS_SENHA) = 'demo12345';

UPDATE TB_AJUDANTE
SET DS_SENHA = '$2a$10$Ed9EVPl00zT5e1oSEUmUq.mPwCjxKhIC4kfO8dSjdBPC7/Xz4Sfeq'
WHERE TRIM(DS_SENHA) = 'apoio12345';

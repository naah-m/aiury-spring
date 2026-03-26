ALTER TABLE tb_ajudante ADD COLUMN login VARCHAR(60);
ALTER TABLE tb_ajudante ADD COLUMN senha VARCHAR(255);

UPDATE tb_ajudante
SET login = COALESCE(login, CONCAT('ajudante.', CAST(id AS VARCHAR))),
    senha = COALESCE(senha, 'apoio12345');

ALTER TABLE tb_ajudante ALTER COLUMN login SET NOT NULL;
ALTER TABLE tb_ajudante ALTER COLUMN senha SET NOT NULL;

ALTER TABLE tb_ajudante ADD CONSTRAINT uk_ajudante_login UNIQUE (login);

ALTER TABLE tb_mensagem ADD COLUMN id_remetente_ajudante BIGINT;
ALTER TABLE tb_mensagem ALTER COLUMN id_remetente DROP NOT NULL;

ALTER TABLE tb_mensagem
    ADD CONSTRAINT fk_mensagem_remetente_ajudante
        FOREIGN KEY (id_remetente_ajudante) REFERENCES tb_ajudante (id);

ALTER TABLE tb_mensagem
    ADD CONSTRAINT ck_mensagem_remetente_exclusivo
        CHECK (
            (id_remetente IS NOT NULL AND id_remetente_ajudante IS NULL)
                OR (id_remetente IS NULL AND id_remetente_ajudante IS NOT NULL)
            );

CREATE INDEX idx_mensagem_remetente_ajudante_data ON tb_mensagem (id_remetente_ajudante, data_envio);

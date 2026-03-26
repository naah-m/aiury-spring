ALTER TABLE tb_ajudante ADD (login VARCHAR2(60 CHAR));
ALTER TABLE tb_ajudante ADD (senha VARCHAR2(255 CHAR));

UPDATE tb_ajudante
SET login = COALESCE(login, 'ajudante.' || TO_CHAR(id)),
    senha = COALESCE(senha, 'apoio12345');

ALTER TABLE tb_ajudante MODIFY (login NOT NULL);
ALTER TABLE tb_ajudante MODIFY (senha NOT NULL);

ALTER TABLE tb_ajudante ADD CONSTRAINT uk_ajudante_login UNIQUE (login);

ALTER TABLE tb_mensagem ADD (id_remetente_ajudante NUMBER(19));
ALTER TABLE tb_mensagem MODIFY (id_remetente NULL);

ALTER TABLE tb_mensagem
    ADD CONSTRAINT fk_mensagem_remetente_ajudante
        FOREIGN KEY (id_remetente_ajudante) REFERENCES tb_ajudante (id);

ALTER TABLE tb_mensagem
    ADD CONSTRAINT ck_mensagem_remetente_exclusivo
        CHECK (
            (id_remetente IS NOT NULL AND id_remetente_ajudante IS NULL)
                OR (id_remetente IS NULL AND id_remetente_ajudante IS NOT NULL)
            );

CREATE INDEX idx_mensagem_rem_ajudante_data ON tb_mensagem (id_remetente_ajudante, data_envio);

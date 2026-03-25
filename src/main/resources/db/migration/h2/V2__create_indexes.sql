CREATE INDEX idx_cidade_estado ON cidade (id_estado);
CREATE INDEX idx_usuario_cidade ON usuario (id_cidade);
CREATE INDEX idx_chat_usuario ON chat (id_usuario);
CREATE INDEX idx_chat_ajudante ON chat (id_ajudante);
CREATE INDEX idx_chat_status ON chat (status);
CREATE INDEX idx_mensagem_chat_data ON tb_mensagem (id_chat, data_envio);
CREATE INDEX idx_mensagem_remetente_data ON tb_mensagem (id_remetente, data_envio);

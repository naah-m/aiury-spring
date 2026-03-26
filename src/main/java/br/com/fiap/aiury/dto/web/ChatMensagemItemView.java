package br.com.fiap.aiury.dto.web;

import java.time.LocalDateTime;

public record ChatMensagemItemView(
        Long id,
        String remetenteNome,
        String remetenteTipo,
        boolean mensagemDoUsuario,
        String texto,
        LocalDateTime dataEnvio
) {
}

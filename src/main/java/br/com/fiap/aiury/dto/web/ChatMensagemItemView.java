package br.com.fiap.aiury.dto.web;

import java.time.LocalDateTime;

public record ChatMensagemItemView(
        Long id,
        String texto,
        LocalDateTime dataEnvio
) {
}

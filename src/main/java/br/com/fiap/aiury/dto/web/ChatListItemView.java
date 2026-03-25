package br.com.fiap.aiury.dto.web;

import br.com.fiap.aiury.entities.ChatStatus;

import java.time.LocalDateTime;

public record ChatListItemView(
        Long id,
        Long usuarioId,
        String usuarioNomeReal,
        String usuarioNomeAnonimo,
        Long ajudanteId,
        String ajudanteAreaAtuacao,
        boolean ajudanteDisponivel,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        ChatStatus status
) {
}

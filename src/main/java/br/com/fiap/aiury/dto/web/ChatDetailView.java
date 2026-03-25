package br.com.fiap.aiury.dto.web;

import br.com.fiap.aiury.entities.ChatStatus;

import java.time.LocalDateTime;

public record ChatDetailView(
        Long id,
        Long usuarioId,
        String usuarioNomeReal,
        String usuarioNomeAnonimo,
        String cidadeNome,
        String estadoUf,
        Long ajudanteId,
        String ajudanteAreaAtuacao,
        boolean ajudanteDisponivel,
        Double ajudanteRating,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        ChatStatus status
) {
}

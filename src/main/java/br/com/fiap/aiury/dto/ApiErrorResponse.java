package br.com.fiap.aiury.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Payload padrao de erro retornado pela API.
 */
@Schema(name = "ApiErrorResponse", description = "Estrutura padrao para respostas de erro")
public record ApiErrorResponse(
        @Schema(description = "Timestamp do erro", example = "2026-03-24T16:30:00") LocalDateTime timestamp,
        @Schema(description = "Codigo HTTP", example = "404") int status,
        @Schema(description = "Descricao resumida do erro", example = "Not Found") String error,
        @Schema(description = "Mensagem detalhada", example = "Usuario nao encontrado com ID: 999") String message,
        @Schema(description = "Path da requisicao", example = "/api/usuarios/999") String path,
        @Schema(description = "Erros de validacao por campo") Map<String, String> validationErrors
) {
}

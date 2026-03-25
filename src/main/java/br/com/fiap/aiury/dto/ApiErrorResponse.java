package br.com.fiap.aiury.dto;

import br.com.fiap.aiury.configs.DateTimePatterns;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Payload padrao de erro retornado pela API.
 */
@Schema(name = "ApiErrorResponse", description = "Estrutura padrao para respostas de erro")
public record ApiErrorResponse(
        @JsonFormat(pattern = DateTimePatterns.DATE_TIME)
        @Schema(
                description = "Timestamp do erro",
                example = "25/03/2026 16:30:00",
                type = "string",
                pattern = DateTimePatterns.DATE_TIME
        ) LocalDateTime timestamp,
        @Schema(description = "Codigo HTTP", example = "404") int status,
        @Schema(description = "Descricao resumida do erro", example = "Not Found") String error,
        @Schema(description = "Mensagem detalhada", example = "Usuario nao encontrado com ID: 999") String message,
        @Schema(description = "Path da requisicao", example = "/api/usuarios/999") String path,
        @Schema(description = "Erros de validacao por campo") Map<String, String> validationErrors
) {
}

package br.com.fiap.aiury.dto;

import br.com.fiap.aiury.configs.DateTimePatterns;
import br.com.fiap.aiury.entities.ChatStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO de entrada para criacao e atualizacao de chat.
 */
@Data
@Schema(name = "ChatRequest", description = "Payload de entrada para criação/atualização de chat")
public class ChatRequestDTO {

    @NotNull(message = "O ID do usuário é obrigatório")
    @Schema(description = "Identificador do usuário vinculado ao chat", example = "10")
    private Long usuarioId;

    @NotNull(message = "O ID do ajudante é obrigatório")
    @Schema(description = "Identificador do ajudante vinculado ao chat", example = "3")
    private Long ajudanteId;

    @NotNull(message = "A data de início é obrigatória")
    @JsonFormat(pattern = DateTimePatterns.DATE_TIME)
    @Schema(
            description = "Data e hora de início no formato DD/MM/AAAA HH:mm:ss",
            example = "25/03/2026 14:00:00",
            type = "string",
            pattern = DateTimePatterns.DATE_TIME
    )
    private LocalDateTime dataInicio;

    @JsonFormat(pattern = DateTimePatterns.DATE_TIME)
    @Schema(
            description = "Data e hora de encerramento no formato DD/MM/AAAA HH:mm:ss",
            example = "25/03/2026 15:05:00",
            nullable = true,
            type = "string",
            pattern = DateTimePatterns.DATE_TIME
    )
    private LocalDateTime dataFim;

    @NotNull(message = "O status do chat é obrigatório")
    @Schema(description = "Status operacional do chat", example = "EM_ANDAMENTO")
    private ChatStatus status;
}


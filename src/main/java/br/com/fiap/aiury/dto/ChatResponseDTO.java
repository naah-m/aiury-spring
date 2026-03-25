package br.com.fiap.aiury.dto;

import br.com.fiap.aiury.entities.ChatStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO de resposta do recurso de chat.
 */
@Data
@Schema(name = "ChatResponse", description = "Representacao de chat retornada pela API")
public class ChatResponseDTO {

    @Schema(description = "Identificador do chat", example = "101")
    private Long id;

    @Schema(description = "ID do usuario do chat", example = "10")
    private Long usuarioId;

    @Schema(description = "ID do ajudante do chat", example = "3")
    private Long ajudanteId;

    @Schema(description = "Data/hora de inicio", example = "2026-03-24T14:00:00")
    private LocalDateTime dataInicio;

    @Schema(description = "Data/hora de encerramento", example = "2026-03-24T15:05:00", nullable = true)
    private LocalDateTime dataFim;

    @Schema(description = "Status atual do chat", example = "EM_ANDAMENTO")
    private ChatStatus status;
}

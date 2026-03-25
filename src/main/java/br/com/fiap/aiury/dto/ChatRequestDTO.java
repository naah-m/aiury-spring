package br.com.fiap.aiury.dto;

import br.com.fiap.aiury.entities.ChatStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO de entrada para criacao e atualizacao de chat.
 */
@Data
@Schema(name = "ChatRequest", description = "Payload de entrada para criacao/atualizacao de chat")
public class ChatRequestDTO {

    @NotNull(message = "O ID do usuario e obrigatorio")
    @Schema(description = "Identificador do usuario vinculado ao chat", example = "10")
    private Long usuarioId;

    @NotNull(message = "O ID do ajudante e obrigatorio")
    @Schema(description = "Identificador do ajudante vinculado ao chat", example = "3")
    private Long ajudanteId;

    @NotNull(message = "A data de inicio e obrigatoria")
    @Schema(description = "Data e hora de inicio do chat", example = "2026-03-24T14:00:00")
    private LocalDateTime dataInicio;

    @Schema(description = "Data e hora de encerramento", example = "2026-03-24T15:05:00", nullable = true)
    private LocalDateTime dataFim;

    @NotNull(message = "O status do chat e obrigatorio")
    @Schema(description = "Status operacional do chat", example = "EM_ANDAMENTO")
    private ChatStatus status;
}

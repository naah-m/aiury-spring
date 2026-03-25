package br.com.fiap.aiury.dto;

import br.com.fiap.aiury.configs.DateTimePatterns;
import br.com.fiap.aiury.entities.ChatStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = DateTimePatterns.DATE_TIME)
    @Schema(
            description = "Data/hora de inicio no formato DD/MM/AAAA HH:mm:ss",
            example = "25/03/2026 14:00:00",
            type = "string",
            pattern = DateTimePatterns.DATE_TIME
    )
    private LocalDateTime dataInicio;

    @JsonFormat(pattern = DateTimePatterns.DATE_TIME)
    @Schema(
            description = "Data/hora de encerramento no formato DD/MM/AAAA HH:mm:ss",
            example = "25/03/2026 15:05:00",
            nullable = true,
            type = "string",
            pattern = DateTimePatterns.DATE_TIME
    )
    private LocalDateTime dataFim;

    @Schema(description = "Status atual do chat", example = "EM_ANDAMENTO")
    private ChatStatus status;
}

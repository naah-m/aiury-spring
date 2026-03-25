package br.com.fiap.aiury.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO de resposta do recurso de mensagem.
 */
@Data
@Schema(name = "MensagemResponse", description = "Representacao de mensagem retornada pela API")
public class MensagemResponseDTO {

    @Schema(description = "Identificador da mensagem", example = "700")
    private Long id;

    @Schema(description = "ID do chat relacionado", example = "101")
    private Long chatId;

    @Schema(description = "ID do remetente", example = "10")
    private Long remetenteId;

    @Schema(description = "Texto enviado", example = "Obrigado pela escuta de hoje.")
    private String texto;

    @Schema(description = "Data e hora de envio", example = "2026-03-24T14:15:00")
    private LocalDateTime dataEnvio;
}
